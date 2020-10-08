package com.example.intimesimple.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.intimesimple.repositories.WorkoutRepository
import timber.log.Timber


class WorkoutDetailViewModel @ViewModelInject constructor(
    private val workoutRepository: WorkoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    fun test(){
        Timber.d("wID: ${savedStateHandle.get<Long>("wId")!!}")
    }
}