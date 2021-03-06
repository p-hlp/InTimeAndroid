package com.example.intimesimple.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: Workout): Long

    suspend fun insertWithTimestamp(workout: Workout){
        insertWorkout(workout.apply {
            createdAt = System.currentTimeMillis()
        })
    }

    @Update
    abstract suspend fun updateWorkout(workout: Workout)

    suspend fun updateWithLastCompletion(workout: Workout){
        updateWorkout(workout.apply {
            lastCompletion = System.currentTimeMillis()
        })
    }

    @Delete
    abstract suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workout_table WHERE id = :wId")
    abstract suspend fun deleteWorkoutWithId(wId: Long)

    @Query("SELECT * from workout_table")
    abstract fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_table WHERE id = :wId")
    abstract fun getWorkoutWithId(wId: Long): Flow<Workout>

    @Query("SELECT * FROM workout_table WHERE id = :wId")
    abstract fun getWorkoutWithIdSingleshot(wId: Long): Workout

    fun getWorkoutDistinctUntilChanged(wId: Long) =
        getWorkoutWithId(wId = wId).distinctUntilChanged()
}