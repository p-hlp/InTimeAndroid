package com.example.intimesimple.ui.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.VolumeButtonState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.repositories.PreferenceRepository
import com.example.intimesimple.repositories.WorkoutRepository
import com.example.intimesimple.services.TimerService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("BinaryOperationInTimber")
class WorkoutDetailViewModel @ViewModelInject constructor(
    private val workoutRepository: WorkoutRepository,
    private val preferenceRepository: PreferenceRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workout = MutableLiveData<Workout?>()

    val volumeButtonState = preferenceRepository.soundStateFlow.asLiveData().map {
        it?.let{ VolumeButtonState.valueOf(it)} ?: VolumeButtonState.MUTE
    }

    // Get immutable LiveData from TimerService singleton
    val timerState: LiveData<TimerState>
        get() = TimerService.timerState

    val workoutState: LiveData<WorkoutState>
        get() = TimerService.workoutState

    val timeInMillis: LiveData<Long>
        get() = TimerService.timeInMillis

    val timerRepCount: LiveData<Int>
        get() = TimerService.repetitionCount

    val progressTime: LiveData<Long>
        get() = TimerService.progressTimeInMillis


    init {
        // Getting first value in flow
        Timber.d("DetaiLViewModel init")
        savedStateHandle.get<Long>("wId")?.let {
            viewModelScope.launch {
                workout.value =
                    workoutRepository.workoutDao.getWorkoutDistinctUntilChanged(it)
                    .first()
            }
        }
    }

    fun setSoundState(state: String) = viewModelScope.launch {
        preferenceRepository.setSoundState(state)
        Timber.d("Set volumeButtonState to: $state")
    }

    fun setCurrentWorkout(wId: Long?){
        wId?.let {
            viewModelScope.launch {
                workout.value = workoutRepository.workoutDao
                        .getWorkoutDistinctUntilChanged(it)
                        .first()
            }
        }
    }

    fun resetCurrentWorkout(){
        workout.value = null
    }
}