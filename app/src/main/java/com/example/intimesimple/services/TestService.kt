package com.example.intimesimple.services

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TestService : LifecycleService(){

    @Inject
    lateinit var workoutRepository: WorkoutRepository
    lateinit var workout: Workout

    companion object{
        val timerState = MutableLiveData<TimerState>()
        val timeInMillis = MutableLiveData<Long>()
        val repetitionCount = MutableLiveData<Int>()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                ACTION_START -> {
                    // TODO: Get WorkoutData via intent extra or query to db
                    it.extras?.let {bundle ->
                        val id = bundle.getLong(EXTRA_WORKOUT_ID)
                        // TODO: get workout from DB
                        // TODO: Post new timerState
                        // TODO: Post new timeInMillis -> workout.exerciseTime
                    }
                }

                ACTION_PAUSE -> {
                    // TODO: Post new timerState
                }

                ACTION_RESUME -> {
                    // TODO: Post new timerState
                }

                ACTION_CANCEL -> {
                    // TODO: Post new timerState
                    // TODO: Post new timeInMillis -> reset to workout.exerciseTime
                }
                else -> {}
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }
}