package com.example.intimesimple.ui.viewmodels

import android.annotation.SuppressLint
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.repositories.WorkoutRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


@SuppressLint("BinaryOperationInTimber")
class WorkoutDetailViewModel @ViewModelInject constructor(
    private val workoutRepository: WorkoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workout = MutableLiveData<Workout>()

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

}