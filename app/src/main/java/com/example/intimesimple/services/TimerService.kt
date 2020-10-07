package com.example.intimesimple.services

import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_NEXT
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_PREVIOUS
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TimerService: LifecycleService(){

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    // LiveData for UI to observe
    companion object {
        val timerState = MutableLiveData<TimerState>()
        val currentRepetition = MutableLiveData<Int>()
        val currentWorkout = MutableLiveData<Workout>()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // handle actions
        intent?.let {
            when(it.action){
                ACTION_START    -> {
                    Timber.d("ACTION_START")
                    startForegroundService()
                }

                ACTION_RESUME   -> {
                    Timber.d("ACTION_RESUME")
                }

                ACTION_PAUSE    -> {
                    Timber.d("ACTION_PAUSE")
                }

                ACTION_CANCEL   -> {
                    Timber.d("ACTION_CANCEL")
                    stopForegroundService()
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
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun stopForegroundService(){
        stopForeground(true)
        stopSelf()
    }
}