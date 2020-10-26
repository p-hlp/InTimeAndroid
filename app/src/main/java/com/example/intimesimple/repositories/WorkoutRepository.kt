package com.example.intimesimple.repositories

import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    val workoutDao: WorkoutDao
){
    suspend fun insertWorkout(workout: Workout) = workoutDao.insertWithTimestamp(workout)

    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)

    suspend fun deleteWorkoutWithId(wId: Long) = workoutDao.deleteWorkoutWithId(wId)

    suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)

    suspend fun updateWorkoutLastCompletion(workout: Workout)
            = workoutDao.updateWithLastCompletion(workout)

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun getWorkout(wId: Long) = workoutDao.getWorkoutDistinctUntilChanged(wId)

    fun getWorkoutSingle(wId: Long) = workoutDao.getWorkoutWithIdSingleshot(wId)
}