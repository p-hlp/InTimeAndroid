package com.example.intimesimple.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.intimesimple.data.local.Timer
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TimerService: LifecycleService(){

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    var currentWorkoutTimer = Timer()

    // LiveData for UI to observe
    companion object {
        val timerState = MutableLiveData<TimerState>()
        val currentRepetition = MutableLiveData<Int>()
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
            when(it.action){
                ACTION_START    -> {
                    Timber.d("ACTION_START")
                    it.extras?.let {bundle ->
                        currentWorkoutTimer.repetitions = bundle.getInt(EXTRA_REPETITION)
                        currentWorkoutTimer.exerciseTime = bundle.getLong(EXTRA_EXERCISETIME)
                        currentWorkoutTimer.pauseTime = bundle.getLong(EXTRA_PAUSETIME)
                    }
                    Timber.d("${currentWorkoutTimer.repetitions} - " +
                            "${currentWorkoutTimer.exerciseTime} - " +
                            "${currentWorkoutTimer.pauseTime}")

                    startForegroundService()
                    timerState.postValue(TimerState.RUNNING)
                }

                ACTION_RESUME   -> {
                    Timber.d("ACTION_RESUME")
                    timerState.postValue(TimerState.RUNNING)
                }

                ACTION_PAUSE    -> {
                    Timber.d("ACTION_PAUSE")
                    timerState.postValue(TimerState.PAUSED)
                }

                ACTION_CANCEL   -> {
                    Timber.d("ACTION_CANCEL")
                    stopForegroundService()
                    timerState.postValue(TimerState.EXPIRED)
                }

                ACTION_NEXT     -> {
                    Timber.d("ACTION_NEXT")
                }

                ACTION_PREVIOUS -> {
                    Timber.d("ACTION_PREVIOUS")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager = notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun stopForegroundService(){
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}