package com.example.intimesimple.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

abstract class WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: Workout)

    suspend fun insertWithTimestamp(workout: Workout){
        insertWorkout(workout.apply {
            createdAt = System.currentTimeMillis()
        })
    }

    @Delete
    abstract suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * from WORKOUT_TABLE")
    abstract  fun getAllWorkouts(): Flow<List<Workout>>
}