package com.example.intimesimple.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.intimesimple.MainActivity
import com.example.intimesimple.R
import com.example.intimesimple.data.local.AudioState
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.di.CancelActionPendingIntent
import com.example.intimesimple.di.PauseActionPendingIntent
import com.example.intimesimple.di.ResumeActionPendingIntent
import com.example.intimesimple.repositories.PreferenceRepository
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.ui.composables.navigation.Screen
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_MUTE
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_SOUND
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.ACTION_VIBRATE
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import com.example.intimesimple.utils.Constants.NOTIFICATION_ID
import com.example.intimesimple.utils.Constants.TIMER_STARTING_IN_TIME
import com.example.intimesimple.utils.Constants.TIMER_UPDATE_INTERVAL
import com.example.intimesimple.utils.Constants.WORKOUT_DETAIL_URI
import com.example.intimesimple.utils.getFormattedStopWatchTime
import com.example.intimesimple.utils.getNextWorkoutState
import com.example.intimesimple.utils.millisToSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TimerService : LifecycleService(), TextToSpeech.OnInitListener
{

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    @ResumeActionPendingIntent
    @Inject
    lateinit var resumeActionPendingIntent: PendingIntent

    @PauseActionPendingIntent
    @Inject
    lateinit var pauseActionPendingIntent: PendingIntent

    @CancelActionPendingIntent
    @Inject
    lateinit var cancelActionPendingIntent: PendingIntent

    @Inject
    lateinit var workoutRepository: WorkoutRepository

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var vibrator: Vibrator

    private var tts: TextToSpeech? = null

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var wakeLock: PowerManager.WakeLock? = null

    private var workout: Workout? = null

    private var firstRun = true
    private var isInitialized = false
    private var isKilled = false
    private var isRunning = false

    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var repetitionIndex = 0

    // keeping internal states, because reading posted LiveData is delayed
    private var internalWorkoutState = WorkoutState.STARTING
    private var internalAudioState = AudioState.MUTE

    companion object{
        val timerState = MutableLiveData<TimerState>()
        val workoutState = MutableLiveData<WorkoutState>()
        val timeInMillis = MutableLiveData<Long>()
        val progressTimeInMillis = MutableLiveData<Long>()
        val repetitionCount = MutableLiveData<Int>()
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")

        currentNotificationBuilder = baseNotificationBuilder
        tts = TextToSpeech(this, this)

        // get audio state from data store
        serviceScope.launch {
            internalAudioState = AudioState.valueOf(preferenceRepository.getCurrentSoundState())
        }

        timerState.postValue(TimerState.EXPIRED)
        internalWorkoutState = WorkoutState.STARTING
        workoutState.postValue(internalWorkoutState)


        // observe timerState and update notification actions
        timerState.observe(this, Observer {
            if(!isKilled && isRunning)
                timerState.value?.let {
                    updateNotificationActions(it)
                }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        tts?.stop()
        tts?.shutdown()
        Timber.d("onDestroy")
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
                            Timber.d("ID From bundles: $id")
                            currentNotificationBuilder
                                    .setContentIntent(buildPendingIntentWithId(id))

                            serviceScope.launch {
                                workout = workoutRepository.getWorkout(id).first().apply {
                                    this.repetitions *= 2
                                }
                                if(!isInitialized){
                                            // Post new timerState
                                            timerState.postValue(TimerState.RUNNING)
                                            workoutState.postValue(WorkoutState.STARTING)
                                            internalWorkoutState = WorkoutState.STARTING
                                            // Post new timeInMillis -> workout.exerciseTime
                                            timeInMillis.postValue(workout?.exerciseTime)
                                            repetitionCount.postValue(workout?.repetitions)
                                            // start foreground service + timer
                                            startForegroundService()
                                            isInitialized = true
                                }
                            }
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
                    stopTimer()
                }

                ACTION_RESUME -> {
                    Timber.d("ACTION_RESUME")
                    // Post new timerState
                    resumeTimer()
                }

                ACTION_CANCEL -> {
                    Timber.d("ACTION_CANCEL")
                    stopForegroundService()
                }

                ACTION_MUTE -> {
                    Timber.d("ACTION_MUTE")
                    internalAudioState = AudioState.MUTE
                }

                ACTION_VIBRATE -> {
                    Timber.d("ACTION_VIBRATE")
                    internalAudioState = AudioState.VIBRATE
                }

                ACTION_SOUND -> {
                    Timber.d("ACTION_SOUND")
                    internalAudioState = AudioState.SOUND
                }
                else -> {}
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    // TTS
    override fun onInit(status: Int) {
        tts?.let{
            if (status == TextToSpeech.SUCCESS) {
                Timber.d("TTS successfully initialized")
                // set UK English as language for tts
                val result = it.setLanguage(Locale.US)
                it.voice = it.defaultVoice
                it.language = Locale.US

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Timber.d( "The Language specified is not supported!")
                }
            } else {
                Timber.d( "Initialization Failed!")
            }
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    private fun startTimer(wasPaused: Boolean){
        // Only start timer if workout is not null
        //Timber.d("Timer Workout - ${workout.hashCode()}")
        workout?.let {
            val time = getTimeWithWorkoutState(wasPaused, it)
            timeInMillis.postValue(time)
            lastSecondTimestamp = time
            //Timber.d("Starting timer... with $time countdown")
            timer = object : CountDownTimer(time, TIMER_UPDATE_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    millisToCompletion = millisUntilFinished
                    progressTimeInMillis.postValue(millisUntilFinished)
                    //Timber.d("timeInMillis $millisToCompletion")
                    if(millisUntilFinished <= lastSecondTimestamp - 1000L){
                        lastSecondTimestamp -= 1000L
                        timeInMillis.postValue(lastSecondTimestamp)
                        if(lastSecondTimestamp <= 3000L){
                            //Timber.d("lastSecondTimeStamp: $lastSecondTimestamp")
                            speakOrVibrate(
                                    millisToSeconds(lastSecondTimestamp).toString(),
                                    200L
                            )
                        }
                    }
                }

                override fun onFinish() {
                    //Timber.d("Timer finished")
                    repetitionIndex += 1
                    if((it.repetitions  - repetitionIndex) > 0){
                        repetitionCount.postValue(repetitionCount.value?.minus(1))
                        //Figure out next workoutState
                        internalWorkoutState = getNextWorkoutState(internalWorkoutState)
                        //Timber.d("Next workoutState: ${internalWorkoutState.stateName}")
                        workoutState.postValue(internalWorkoutState)
                        startTimer(false)

                        speakOrVibrate(
                                internalWorkoutState.stateName,
                                500L)
                    }else stopForegroundService()
                }

            }.start()
        }
    }

    private fun getTimeWithWorkoutState(
        wasPaused: Boolean,
        workout: Workout
    ): Long {
        return if (wasPaused) millisToCompletion
        else {
            when (internalWorkoutState) {
                WorkoutState.STARTING -> TIMER_STARTING_IN_TIME
                WorkoutState.BREAK -> workout.pauseTime
                else -> workout.exerciseTime
            }
        }
    }

    private fun stopTimer(){
        timerState.postValue(TimerState.PAUSED)
        timer?.cancel()
    }

    private fun resumeTimer(){
        timerState.postValue(TimerState.RUNNING)
        startTimer(true)
    }

    private fun startForegroundService(){
        isRunning = true

        // Get wakelock
        wakeLock =
                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                            "com.example.intimesimple.services:TimerService::lock").apply {
                        acquire()
                    }
                }
        Timber.d("Acquired wakelock")

        startTimer(false)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager = notificationManager)
        }

        Timber.d("Starting foregroundService")
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        // Observe timeInMillis and update notification
        timeInMillis.observe(this, Observer {
            if (!isKilled && isRunning){
                workout?.let {wo ->
                    val notification = currentNotificationBuilder
                            .setContentTitle(wo.name)
                            .setContentText(getFormattedStopWatchTime(it))

                    notificationManager.notify(NOTIFICATION_ID, notification.build())
                }
            }
        })
    }

    private fun stopForegroundService(){
        Timber.d("Stopping foregroundService")
        // Release wakelock
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                    Timber.d("Released wakelock")
                }
            }
        } catch (e: Exception) {
            Timber.d("Wasn't able to release wakelock ${e.message}")
        }

        timer?.cancel()
        timerState.postValue(TimerState.EXPIRED)
        workout?.let {
            // Reset timeInMillis -> workout.exerciseTime
            timeInMillis.postValue(it.exerciseTime)
            progressTimeInMillis.postValue(it.exerciseTime)
        }

        isKilled = true
        isRunning = false
        repetitionIndex = 0
        firstRun = true
        stopForeground(true)
        stopSelf()
    }

    private fun getNextWorkoutState(current: WorkoutState) = when(current){
        WorkoutState.STARTING -> WorkoutState.WORK
        WorkoutState.WORK -> WorkoutState.BREAK
        WorkoutState.BREAK -> WorkoutState.WORK
    }


    private fun updateNotificationActions(state: TimerState){
        // Updates actions of current notification depending on TimerState
        val notificationActionText = if(state == TimerState.RUNNING) "Pause" else "Resume"

        // Build pendingIntent depending on TimerState
        val pendingIntent = if(state == TimerState.RUNNING){
            pauseActionPendingIntent
        }else{
            resumeActionPendingIntent
        }

        // Get notificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // Clear current actions
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // Set Action, icon seems irrelevant
        currentNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.ic_alarm, notificationActionText, pendingIntent)
                .addAction(R.drawable.ic_alarm, "Cancel", cancelActionPendingIntent)
        notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
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

    private fun buildPendingIntentWithId(id: Long): PendingIntent {
        Timber.d("buildPendingIntentWithId - id: $id")
        return PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java).also {
                    it.action = Constants.ACTION_SHOW_MAIN_ACTIVITY
                    it.putExtra(EXTRA_WORKOUT_ID, id)
                    //Set data uri to deeplink uri -> automatically navigates when navGraph is created
                    it.data = Uri.parse(WORKOUT_DETAIL_URI + "$id")
                },
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun speakOrVibrate(sayText: String, vLength: Long){
        when(internalAudioState){
            AudioState.MUTE -> return
            AudioState.VIBRATE -> vibrate(vLength)
            AudioState.SOUND -> ttsSpeak(sayText)
        }
    }

    private fun vibrate(ms: Long){
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(ms) // Vibrate method for below API Level 26
            }
        }
    }

    private fun ttsSpeak(message: String){
        Timber.d("Trying to speak: $message")
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}