package com.example.intimesimple.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.intimesimple.repositories.WorkoutRepository

class WorkoutListViewModel @ViewModelInject constructor(
        private val workoutRepository: WorkoutRepository
): ViewModel() {

    val workouts = workoutRepository.getAllWorkouts().asLiveData()

}