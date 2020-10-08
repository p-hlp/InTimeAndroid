package com.example.intimesimple.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.intimesimple.repositories.WorkoutRepository


class WorkoutDetailViewModel @ViewModelInject constructor(
    workoutRepository: WorkoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Uses savedStateHandle to access safeargs, until we have proper support for AssistedInject
    val workout = workoutRepository.getWorkout(savedStateHandle.get<Long>("wId")!!)

}