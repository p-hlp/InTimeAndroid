package com.example.intimesimple.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intimesimple.repositories.WorkoutRepository
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject

class WorkoutDetailViewModel @AssistedInject constructor(
    val workoutRepository: WorkoutRepository,
    @Assisted private val wId: Long
) : ViewModel(){

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(wId: Long): WorkoutDetailViewModel
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