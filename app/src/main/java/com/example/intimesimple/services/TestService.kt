package com.example.intimesimple.services

import android.app.PendingIntent
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.PowerManager
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.intimesimple.data.local.AudioState
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.di.CancelActionPendingIntent
import com.example.intimesimple.di.PauseActionPendingIntent
import com.example.intimesimple.di.ResumeActionPendingIntent
import com.example.intimesimple.repositories.PreferenceRepository
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_CANCEL_AND_RESET
import com.example.intimesimple.utils.Constants.ACTION_INITIALIZE_DATA
import com.example.intimesimple.utils.Constants.ACTION_MUTE
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_SOUND
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.ACTION_VIBRATE
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import com.example.intimesimple.utils.Constants.TIMER_UPDATE_INTERVAL
import com.example.intimesimple.utils.buildMainActivityPendingIntentWithId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TestService : LifecycleService(), TextToSpeech.OnInitListener{

    // notification builder
    @Inject lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    // pending intents for notification action-handling
    @ResumeActionPendingIntent @Inject lateinit var resumeActionPendingIntent: PendingIntent
    @PauseActionPendingIntent @Inject lateinit var pauseActionPendingIntent: PendingIntent
    @CancelActionPendingIntent @Inject lateinit var cancelActionPendingIntent: PendingIntent

    // repositories
    @Inject lateinit var workoutRepository: WorkoutRepository
    @Inject lateinit var preferenceRepository: PreferenceRepository

    // current workout
    private var workout: Workout? = null

    // service state
    private var isInitialized = false
    private var isKilled = true
    private var isTimerRunning = false
    private var isBound = false
    private var audioState = AudioState.MUTE

    // timer
    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var timerIndex = 0

    // audio/tts
    @Inject lateinit var vibrator: Vibrator
    private var tts: TextToSpeech? = null

    // utility
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var wakeLock: PowerManager.WakeLock? = null

    companion object{
        // holds MutableLiveData for UI to observe
        val currentWorkout = MutableLiveData<Workout>()
        val currentWorkoutState = MutableLiveData<WorkoutState>()
        val currentRepetition = MutableLiveData<Int>()
        val elapsedTimeInMillis = MutableLiveData<Long>()
        val elapsedTimeInMillisEverySecond = MutableLiveData<Long>()
    }


    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        // Initialize notificationBuilder & TTS class
        currentNotificationBuilder = baseNotificationBuilder
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        /* Initialize TTS here */
        Timber.i("onInit")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle action from the activity
        intent?.let{
            when(it.action){
                // Timer related actions
                ACTION_INITIALIZE_DATA -> {
                    /*Is called when navigating from ListScreen to DetailScreen, fetching data
                    * from database here -> data initialization, sets isInitialized = true*/
                    Timber.i("ACTION_INITIALIZE_DATA")
                    initializeData(it)
                }
                ACTION_START -> {
                    /*This is called when Start-Button is pressed, starting timer here and setting
                    * isTimerRunning = true*/
                    //startTimer()
                    Timber.i("ACTION_START")
                }
                ACTION_PAUSE -> {
                    /*Called when pause button is pressed, pause timer, set isTimerRunning = false*/
                    //pauseTimer()
                    Timber.i("ACTION_PAUSE")
                }
                ACTION_RESUME -> {
                    /*Called when resume button is pressed, resume timer here, set isTimerRunning
                    * = true*/
                    //resumeTimer()
                    Timber.i("ACTION_RESUME")
                }
                ACTION_CANCEL -> {
                    /*This i called when cancel button is pressed - "reset" the current timer to
                    * start state -> timerRunning = false, reset repetition index all posted
                    * values*/
                    //timer?.cancel()
                    Timber.i("ACTION_CANCEL")
                }
                ACTION_CANCEL_AND_RESET -> {
                    /*Is called when navigating back to ListsScreen, resetting acquired data
                    * to null*/
                    Timber.i("ACTION_CANCEL_AND_RESET")
                    resetData()
                }

                // Audio related actions
                ACTION_MUTE -> {
                    /*Sets current audio state to mute*/
                    Timber.i("ACTION_MUTE")
                    audioState = AudioState.MUTE
                }
                ACTION_VIBRATE -> {
                    /*Sets current audio state to vibrate*/
                    Timber.i("ACTION_VIBRATE")
                    audioState = AudioState.VIBRATE
                }
                ACTION_SOUND -> {
                    /*Sets current audio state to sound enabled*/
                    Timber.i("ACTION_SOUND")
                    audioState = AudioState.SOUND
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        // UI is visible, use service without being foreground
        Timber.i("onBind")
        isBound = true
        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        // UI is visible again, push service to background -> notification are not visible
        Timber.i("onRebind")
        isBound = true
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // UI is not visible anymore, push service to foreground -> notifications visible
        Timber.i("onUnbind")
        isBound = false
        // return true so onRebind is used if service is alive and client connects
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        // cancel coroutine job and TTS
        serviceJob.cancel()
        tts?.stop()
        tts?.shutdown()
    }

    private fun startTimer(){
        // time to count down

        val timeInMillis = 10000L

        timer = object : CountDownTimer(timeInMillis, TIMER_UPDATE_INTERVAL){
            override fun onTick(millisUntilFinished: Long) {
                /*handle what happens on every tick/interval of TIMER_UPDATE_INTERVAL*/

            }

            override fun onFinish() {
                /*handle finishing of a timer
                * start new one if there are still repetition left*/

            }

        }
    }

    private fun pauseTimer(){}

    private fun resumeTimer(){}

    private fun cancelTimer(){}

    private fun resetTimer(){}

    private fun initializeData(intent: Intent){
        if(!isInitialized){
            intent.extras?.let {
                val id = it.getLong(EXTRA_WORKOUT_ID)
                if(id != -1L){
                    // id is valid
                    currentNotificationBuilder
                        .setContentIntent(buildMainActivityPendingIntentWithId(id, this))

                    // launch coroutine, fetch workout from db & audiostate from data store
                    serviceScope.launch {
                        workout = workoutRepository.getWorkout(id).first()
                        audioState = AudioState.valueOf(preferenceRepository.getCurrentSoundState())
                        isInitialized = true
                        postInitializedData()
                    }
                }
            }
        }
    }

    private fun postInitializedData(){
        /*Post current data to MutableLiveData*/
    }

    private fun resetData(){
        isInitialized = false
        workout = null
    }
}