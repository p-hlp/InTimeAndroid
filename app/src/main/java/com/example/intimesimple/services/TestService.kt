package com.example.intimesimple.services

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TestService : LifecycleService(){

    @Inject
    lateinit var workoutRepository: WorkoutRepository

    private var workout: Workout? = null

    private var firstRun = true
    private var isInitialized = false

    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var repetitionIndex = 0

    companion object{
        val timerState = MutableLiveData<TimerState>()
        val timeInMillis = MutableLiveData<Long>()
        val repetitionCount = MutableLiveData<Int>()
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
    }


    @SuppressLint("BinaryOperationInTimber")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                ACTION_START -> {
                    Timber.d("ACTION_START - firstRun: $firstRun")
                    if(firstRun){
                       // First run, fetch workout from db, start service
                        it.extras?.let {bundle ->
                            val id = bundle.getLong(EXTRA_WORKOUT_ID)
                            // Get workout from DB
                            workoutRepository.getWorkout(id).asLiveData()
                                    .observe(this, Observer {wo ->
                                        workout = wo
                                        if(!isInitialized){
                                            // Post new timerState
                                            timerState.postValue(TimerState.RUNNING)
                                            // Post new timeInMillis -> workout.exerciseTime
                                            timeInMillis.postValue(workout?.exerciseTime)
                                            isInitialized = true
                                        }
                                    })
                            // start foreground service + timer
                            startForegroundService()

                        }
                        firstRun = false
                    }else{
                        // Reset timerState
                        timerState.postValue(TimerState.RUNNING)
                        workout?.let {wo ->
                            // Reset timeInMillis -> workout.exerciseTime
                            timeInMillis.postValue(wo.exerciseTime)
                        }

                        // start Timer, service already running
                        startTimer(false)
                    }
                }

                ACTION_PAUSE -> {
                    Timber.d("ACTION_PAUSE")
                    // Post new timerState
                    timerState.postValue(TimerState.PAUSED)
                    stopTimer()
                }

                ACTION_RESUME -> {
                    Timber.d("ACTION_RESUME")
                    // Post new timerState
                    timerState.postValue(TimerState.RUNNING)
                    startTimer(true)
                }

                ACTION_CANCEL -> {
                    Timber.d("ACTION_CANCEL")
                    timerState.postValue(TimerState.EXPIRED)
                    workout?.let {wo ->
                        // Reset timeInMillis -> workout.exerciseTime
                        timeInMillis.postValue(wo.exerciseTime)
                    }
                    stopForegroundService()
                }
                else -> {}
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startTimer(wasPaused: Boolean){
        // Only start timer if workout is not null
        workout?.let {
            val time = if (wasPaused) millisToCompletion else it.exerciseTime
            lastSecondTimestamp = time

            timer = object : CountDownTimer(time, Constants.ONE_SECOND) {
                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished <= lastSecondTimestamp - 1000L) {
                        timeInMillis.postValue(lastSecondTimestamp - 1000L)
                    }
                    millisToCompletion = millisUntilFinished
                    Timber.d("timeInMillis ${timeInMillis.value!!}")
                }

                override fun onFinish() {
                    if (repetitionIndex < it.repetitions) {
                        repetitionIndex += 1
                        startTimer(false)
                    } else stopForegroundService()
                }
            }.start()
        }
    }

    private fun stopTimer(){
        timer?.cancel()
    }

    private fun startForegroundService(){

    }

    private fun stopForegroundService(){

    }
}