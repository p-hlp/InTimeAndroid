package com.example.intimesimple.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intimesimple.repositories.WorkoutRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject


class WorkoutDetailViewModel @AssistedInject constructor(
    workoutRepository: WorkoutRepository,
    @Assisted private val workoutId: Long
) : ViewModel() {

    val workout = workoutRepository.getWorkout(workoutId)

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(workoutId: Long): WorkoutDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            workoutId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(workoutId) as T
            }
        }
    }
}