package com.example.intimesimple.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutDao
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.getFormattedStopWatchTime
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
){
    // db queries
    suspend fun insertWorkout(workout: Workout) = workoutDao.insertWithTimestamp(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)
    suspend fun deleteWorkoutWithId(wId: Long) = workoutDao.deleteWorkoutWithId(wId)
    suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)
    suspend fun updateWorkoutLastCompletion(workout: Workout)
            = workoutDao.updateWithLastCompletion(workout)
    fun getAllWorkouts() = workoutDao.getAllWorkouts()
    fun getWorkout(wId: Long) = workoutDao.getWorkoutDistinctUntilChanged(wId)
    fun getWorkoutSingle(wId: Long) = workoutDao.getWorkoutWithIdSingleshot(wId)

    // return immutable livedata from timer service
    fun getTimerServiceWorkoutState() = TimerService.currentWorkoutState as LiveData<WorkoutState>
    fun getTimerServiceTimerState() = TimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceRepetition() = TimerService.currentRepetition as LiveData<Int>
    fun getTimerServiceElapsedTimeMillisESeconds ()
            = TimerService.elapsedTimeInMillisEverySecond as LiveData<Long>
    fun getTimerServiceElapsedTimeMillis () = TimerService.elapsedTimeInMillis as LiveData<Long>
}