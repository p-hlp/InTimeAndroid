package com.example.intimesimple.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.repositories.WorkoutRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class WorkoutListViewModel @ViewModelInject constructor(
        private val workoutRepository: WorkoutRepository
): ViewModel() {

    enum class WorkoutListScreenState{
        List, AddItem
    }

    val workoutListScreenState: LiveData<WorkoutListScreenState>
        get() = _screenState

    private val _screenState = MutableLiveData(WorkoutListScreenState.List)

    val workouts = workoutRepository.getAllWorkouts().asLiveData()

    fun setScreenState(workoutListScreenState: WorkoutListScreenState){
        _screenState.value = workoutListScreenState
        Timber.d("Setting screenState: ${_screenState.value}")
    }

    fun addWorkout(workout: Workout){
        viewModelScope.launch {
            workoutRepository.workoutDao.insertWithTimestamp(workout)
        }
    }

    fun deleteWorkout(workout: Workout){
        Timber.d("Deleting workout: ${workout.id}")
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