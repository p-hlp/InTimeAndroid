package com.example.intimesimple.services

import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.speech.tts.TextToSpeech
import androidx.lifecycle.LifecycleService
import com.example.intimesimple.data.local.AudioState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_CANCEL_AND_RESET
import com.example.intimesimple.utils.Constants.ACTION_INITIALIZE_DATA
import com.example.intimesimple.utils.Constants.ACTION_MUTE
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_SOUND
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.ACTION_VIBRATE

class TestService : LifecycleService(), TextToSpeech.OnInitListener{

    // current workout
    private var workout: Workout? = null

    // service state variables
    private var isInitialized = false
    private var isKilled = true
    private var isTimerRunning = false
    private var isBound = false
    private var audioState = AudioState.MUTE

    // timer variables
    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var timerIndex = 0

    override fun onCreate() {
        super.onCreate()
    }

    override fun onInit(status: Int) {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle action from the activity
        intent?.let{
            when(it.action){
                // Timer related actions
                ACTION_INITIALIZE_DATA -> {
                    /*Is called when navigating from ListScreen to DetailScreen, fetching data
                    * from database here -> data initialization, sets isInitialized = true*/
                }
                ACTION_START -> {
                    /*This is called when Start-Button is pressed, starting timer here and setting
                    * isTimerRunning = true*/
                    //startTimer()
                }
                ACTION_PAUSE -> {
                    /*Called when pause button is pressed, pause timer, set isTimerRunning = false*/
                    //pauseTimer()
                }
                ACTION_RESUME -> {
                    /*Called when resume button is pressed, resume timer here, set isTimerRunning
                    * = true*/
                    //resumeTimer()
                }
                ACTION_CANCEL -> {
                    /*This i called when cancel button is pressed - "reset" the current timer to
                    * start state -> timerRunning = false, reset repetition index all posted
                    * values*/
                    //timer?.cancel()
                }
                ACTION_CANCEL_AND_RESET -> {
                    /*Is called when navigating back to ListsScreen, resetting acquired data
                    * to null*/
                }

                // Audio related actions
                ACTION_MUTE -> {
                    /*Sets current audio state to mute*/
                }
                ACTION_VIBRATE -> {
                    /*Sets current audio state to vibrate*/
                }
                ACTION_SOUND -> {
                    /*Sets current audio state to sound enabled*/
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        // UI is visible, use service without being foreground

        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        // UI is visible again, push service to background -> notification are not visible

        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // UI is not visible anymore, push service to foreground -> notifications visible


        // return true so onRebind is used if service is alive and client connects
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}