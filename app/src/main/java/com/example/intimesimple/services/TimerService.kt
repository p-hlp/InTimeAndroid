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
import com.example.intimesimple.data.local.Timer
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_NEXT
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_PREVIOUS
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.EXTRA_EXERCISETIME
import com.example.intimesimple.utils.Constants.EXTRA_PAUSETIME
import com.example.intimesimple.utils.Constants.EXTRA_REPETITION
import com.example.intimesimple.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.intimesimple.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.intimesimple.utils.Constants.NOTIFICATION_ID
import com.example.intimesimple.utils.Constants.ONE_SECOND
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : LifecycleService() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    private lateinit var timer: CountDownTimer

    private var currentWorkoutTimer = Timer()
    private var isFirstRun = true
    private var isKilled = false
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var repetitionIndex = 0

    // LiveData for UI to observe
    companion object {
        val timerState = MutableLiveData<TimerState>()
        val currentRepetition = MutableLiveData<Int>()
        val timeLeftInMillis = MutableLiveData<Long>()
        val timeLeftInSeconds = MutableLiveData<Int>()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        timerState.postValue(TimerState.EXPIRED)
        currentRepetition.postValue(0)
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // handle actions
        intent?.let { it ->
            when (it.action) {
                ACTION_START -> {
                    Timber.d("ACTION_START")
                    if (isFirstRun) {
                        // if first run get info from bundle, start service & timer
                        it.extras?.let { bundle ->
                            currentWorkoutTimer.repetitions = bundle.getInt(EXTRA_REPETITION)
                            currentWorkoutTimer.exerciseTime = bundle.getLong(EXTRA_EXERCISETIME)
                            currentWorkoutTimer.pauseTime = bundle.getLong(EXTRA_PAUSETIME)
                        }
                        startForegroundService()
                    } else startTimer(false)
                }

                ACTION_RESUME -> {
                    Timber.d("ACTION_RESUME")
                    resumeTimer()
                }

                ACTION_PAUSE -> {
                    Timber.d("ACTION_PAUSE")
                    pauseTimer()
                }

                ACTION_CANCEL -> {
                    Timber.d("ACTION_CANCEL")
                    stopForegroundService()
                }

                ACTION_NEXT -> {
                    Timber.d("ACTION_NEXT")
                }

                ACTION_PREVIOUS -> {
                    Timber.d("ACTION_PREVIOUS")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startTimer(wasPaused: Boolean) {
        timerState.postValue(TimerState.RUNNING)
        val time = if (wasPaused) millisToCompletion else currentWorkoutTimer.exerciseTime
        lastSecondTimestamp = time
        Timber.d("Starting Timer with $time ms countdown on repIndex $repetitionIndex")
        timeLeftInMillis.postValue(time)
        timeLeftInSeconds.postValue((lastSecondTimestamp / ONE_SECOND).toInt())
        timer = object : CountDownTimer(time, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= lastSecondTimestamp - 1000L) {
                    timeLeftInSeconds.postValue(timeLeftInSeconds.value!! - 1)
                }
                timeLeftInMillis.postValue(millisUntilFinished)
                millisToCompletion = millisUntilFinished
                Timber.d("millisUntilFinished $millisUntilFinished")
            }

            override fun onFinish() {
                if (repetitionIndex < currentWorkoutTimer.repetitions) {
                    repetitionIndex += 1
                    startTimer(false)
                } else stopForegroundService()
            }
        }.start()
    }

    private fun resumeTimer() {
        startTimer(true)
        timerState.postValue(TimerState.RUNNING)
    }

    private fun pauseTimer() {
        timer.cancel()
        timerState.postValue(TimerState.PAUSED)
    }

    private fun startForegroundService() {
        Timber.d("Starting ForegroundService")
        startTimer(false)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager = notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun resetTimer() {
        timer.cancel()
        timerState.postValue(TimerState.EXPIRED)
        millisToCompletion = currentWorkoutTimer.exerciseTime
        timeLeftInSeconds.postValue((currentWorkoutTimer.exerciseTime / 1000L).toInt())
        timeLeftInMillis.postValue(currentWorkoutTimer.exerciseTime)
    }

    private fun stopForegroundService() {
        resetTimer()
        isKilled = true
        isFirstRun = true
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}