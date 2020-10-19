package com.example.intimesimple.utils

import androidx.compose.ui.graphics.vector.VectorAsset
import com.example.intimesimple.data.local.AudioState
import com.example.intimesimple.data.local.VolumeButtonState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.utils.Constants.ACTION_MUTE
import com.example.intimesimple.utils.Constants.ACTION_SOUND
import com.example.intimesimple.utils.Constants.ACTION_VIBRATE
import com.example.intimesimple.utils.Constants.ONE_SECOND
import com.example.intimesimple.utils.Constants.TIMER_STARTING_IN_TIME
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max

fun calculateRadiusOffset(
        strokeSize: Float,
        dotStrokeSize: Float,
        markerStrokeSize: Float
): Float {
    return max(strokeSize, max(dotStrokeSize, markerStrokeSize))
}

fun getFormattedStopWatchTime(ms: Long?): String{
    ms?.let {
        var milliseconds = ms

        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        // Build formatted String
        return "${if(hours < 10) "0" else ""}$hours : " +
                "${if(minutes < 10) "0" else ""}$minutes : " +
                "${if(seconds < 10) "0" else ""}$seconds"
    }
    return ""
}

fun getFormattedCompletionTime(ms: Long?): String{
    ms?.let {
        var milliseconds = ms
        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return (if(hours <= 0) "" else if(hours < 10) "0$hours:" else "$hours:") +
                (if(minutes <= 0) "" else if(minutes < 10) "0$minutes:" else "$minutes:" ) +
                "${if(seconds < 10) "0" else ""}$seconds" +
                if(hours > 0) " h" else if(minutes > 0) " min" else "sec"
    }
    return ""
}

fun convertLongToTime(time: Long?): String {
    time?.let {
        val date = Date(time)
        val format = DateFormat.getDateTimeInstance()
        return format.format(date)
    }
    return "No time found!"
}

fun convertDateToLong(date: String?): Long {
    date?.let {
        val df = DateFormat.getDateTimeInstance()
        df.parse(date)?.let {
            return it.time
        }
    }
    return 0L
}

fun millisToSeconds(ms: Long) = (ms/ONE_SECOND).toInt()

fun getNextWorkoutState(current: WorkoutState) = when(current){
        WorkoutState.STARTING -> WorkoutState.WORK
        WorkoutState.WORK -> WorkoutState.BREAK
        WorkoutState.BREAK -> WorkoutState.WORK
}

fun getNextAudioStateAction(audioState: AudioState) = when(audioState){
    AudioState.MUTE -> ACTION_VIBRATE
    AudioState.VIBRATE -> ACTION_SOUND
    AudioState.SOUND -> ACTION_MUTE
}

fun audioStateToIcon(audioState: AudioState) = when(audioState){
    AudioState.MUTE -> VolumeButtonState.MUTE.asset
    AudioState.VIBRATE -> VolumeButtonState.VIBRATE.asset
    AudioState.SOUND -> VolumeButtonState.SOUND.asset
}

fun getCompletionTimeForWorkout(workout: Workout): String {
    val reps = workout.repetitions
    val pauses = workout.repetitions - 1
    return getFormattedCompletionTime(
            reps * workout.exerciseTime + pauses * workout.pauseTime
    )
}

fun getNextVolumeButtonState(state: VolumeButtonState) = when(state){
    VolumeButtonState.MUTE -> VolumeButtonState.VIBRATE
    VolumeButtonState.VIBRATE -> VolumeButtonState.SOUND
    VolumeButtonState.SOUND -> VolumeButtonState.MUTE
}

fun getTimerActionFromVolumeButtonState(state: VolumeButtonState) = when(state){
    VolumeButtonState.MUTE -> ACTION_MUTE
    VolumeButtonState.VIBRATE -> ACTION_VIBRATE
    VolumeButtonState.SOUND -> ACTION_SOUND
}

val defaultWorkouts = listOf(
        Workout("15min Posture", 35000L, 15000L, 18),
        Workout("Morning Yoga", 30000L, 15000L, 12),
        Workout("Upper Body", 40000L, 20000L, 8),
        Workout("Lower Body", 40000L, 20000L, 8),
        Workout("Core Routine", 35000L, 25000L, 9),
        Workout("Conditioning", 50000L, 20000L, 5),
        Workout("15min Posture", 35000L, 15000L, 18),
        Workout("Morning Yoga", 30000L, 15000L, 12),
        Workout("Upper Body", 40000L, 20000L, 8),
        Workout("Lower Body", 40000L, 20000L, 8),
        Workout("Core Routine", 35000L, 25000L, 9),
        Workout("Conditioning", 50000L, 20000L, 5),
)

fun getRandomWorkout() = defaultWorkouts.random()