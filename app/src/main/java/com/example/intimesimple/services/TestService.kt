package com.example.intimesimple.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
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
import com.example.intimesimple.utils.Constants.ONE_SECOND
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TestService : LifecycleService(){

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

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
                            // Get workout from DB, maybe oneShot with runBlocking
                            workoutRepository.getWorkout(id).asLiveData()
                                    .observe(this, Observer {wo ->
                                        Timber.d("Workout data changed")
                                        workout = wo
                                        if(!isInitialized){
                                            // Post new timerState
                                            timerState.postValue(TimerState.RUNNING)
                                            // Post new timeInMillis -> workout.exerciseTime
                                            timeInMillis.postValue(workout?.exerciseTime)
                                            repetitionCount.postValue(workout?.repetitions)
                                            // start foreground service + timer
                                            startForegroundService()
                                            isInitialized = true
                                        }
                                    })
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
                    stopForegroundService()
                }
                else -> {}
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startTimer(wasPaused: Boolean){
        // Only start timer if workout is not null
        Timber.d("Timer Workout - ${workout.hashCode()}")
        workout?.let {
            val time = if (wasPaused) millisToCompletion else it.exerciseTime
            timeInMillis.postValue(time)
            lastSecondTimestamp = time
            Timber.d("Starting timer... with $time countdown")
            timer = object : CountDownTimer(time, ONE_SECOND) {
                override fun onTick(millisUntilFinished: Long) {
                    millisToCompletion = millisUntilFinished
                    Timber.d("timeInMillis $millisToCompletion")
                    if(millisUntilFinished <= lastSecondTimestamp - 1000L){
                        timeInMillis.postValue(lastSecondTimestamp - 1000L)
                        lastSecondTimestamp -= 1000L
                    }

                }

                override fun onFinish() {
                    Timber.d("Timer finished")
                    if(it.repetitions - repetitionIndex > 0){
                        repetitionIndex += 1
                        repetitionCount.postValue(repetitionCount.value?.minus(1))
                        startTimer(false)
                    }else stopForegroundService()
                }
            }.start()
        }
    }

    private fun stopTimer(){
        timer?.cancel()
    }

    private fun startForegroundService(){
        startTimer(false)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager = notificationManager)
        }

        Timber.d("Starting foregroundService")
        startForeground(Constants.NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun stopForegroundService(){
        Timber.d("Stopping foregroundService")
        timer?.cancel()
        timerState.postValue(TimerState.EXPIRED)
        workout?.let {
            // Reset timeInMillis -> workout.exerciseTime
            timeInMillis.postValue(it.exerciseTime)
        }
        repetitionIndex = 0
        firstRun = true
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}