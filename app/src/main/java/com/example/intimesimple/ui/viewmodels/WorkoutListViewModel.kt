package com.example.intimesimple.ui.viewmodels

import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutListViewModel @ViewModelInject constructor(
        private val workoutRepository: WorkoutRepository
): ViewModel() {

    val workouts = workoutRepository.getAllWorkouts().asLiveData()

    fun addWorkout(workout: Workout){
        viewModelScope.launch {
            workoutRepository.workoutDao.insertWithTimestamp(workout)
        }
    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch {
            workoutRepository.workoutDao.deleteWorkout(workout)
        }
    }

    fun deleteWorkoutWithId(wId: Long){
        viewModelScope.launch {
            workoutRepository.workoutDao.deleteWorkoutWithId(wId)
        }
    }
}