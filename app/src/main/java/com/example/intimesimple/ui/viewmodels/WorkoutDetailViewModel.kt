package com.example.intimesimple.ui.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.VolumeButtonState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.repositories.PreferenceRepository
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.utils.Constants.TIMER_STARTING_IN_TIME
import com.example.intimesimple.utils.getFormattedStopWatchTime
import kotlinx.coroutines.launch
import timber.log.Timber


class WorkoutDetailViewModel @ViewModelInject constructor(
    private val workoutRepository: WorkoutRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val workout: LiveData<Workout?>
        get() = TimerService.currentWorkout

    val volumeButtonState = preferenceRepository.soundStateFlow.asLiveData().map {
        it?.let{ VolumeButtonState.valueOf(it)} ?: VolumeButtonState.MUTE
    }

    /*TODO: Add abstraction through repository*/

    // Get immutable LiveData from TimerService singleton
    val timerState: LiveData<TimerState>
        get() = TimerService.currentTimerState

    val workoutState: LiveData<WorkoutState>
        get() = TimerService.currentWorkoutState

    val repString: LiveData<String>
        get() = TimerService.currentRepetition.map {
            if(workout.value != null && it != -1) "$it/${workout.value?.repetitions}"
            else ""
        }

    val timeString: LiveData<String>
        get() = TimerService.elapsedTimeInMillisEverySecond.map {
            if(workout.value != null){
                if(timerState.value != TimerState.EXPIRED)
                    getFormattedStopWatchTime(it)
                else
                    getFormattedStopWatchTime(TIMER_STARTING_IN_TIME)
            }else ""
        }

    val elapsedTime: LiveData<Long>
        get() = TimerService.elapsedTimeInMillis.map {
            //Timber.i("elapsedTime: $it")
            if(timerState.value != TimerState.EXPIRED)
                it
            else
                TIMER_STARTING_IN_TIME
        }

    val totalTime: LiveData<Long>
        get() = TimerService.currentWorkoutState.map {
            Timber.i("totalTime: ${it.stateName}")
            if (timerState.value == TimerState.EXPIRED)
                TIMER_STARTING_IN_TIME
            else
                when(it){
                    WorkoutState.BREAK -> {
                        workout.value?.pauseTime ?: 0L
                    }
                    WorkoutState.WORK -> {
                        workout.value?.exerciseTime ?: 0L
                    }
                    else -> {TIMER_STARTING_IN_TIME}
                }
        }

    fun setSoundState(state: String) = viewModelScope.launch {
        preferenceRepository.setSoundState(state)
        Timber.d("Set volumeButtonState to: $state")
    }
}