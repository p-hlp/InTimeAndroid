package com.example.intimesimple.repositories

import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutDao

class WorkoutRepository constructor(
    private val workoutDao: WorkoutDao
){
    suspend fun insertWorkout(workout: Workout) = workoutDao.insertWithTimestamp(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)
    suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)
    fun getAllWorkouts() = workoutDao.getAllWorkouts()
}