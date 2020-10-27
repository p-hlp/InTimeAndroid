package com.example.intimesimple.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.intimesimple.MainActivity
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.intimesimple.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.intimesimple.utils.Constants.WORKOUT_DETAIL_URI
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(notificationManager: NotificationManager) {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW
    )
    notificationManager.createNotificationChannel(channel)
}

fun buildMainActivityPendingIntentWithId(id: Long, context: Context): PendingIntent {
    Timber.d("buildPendingIntentWithId - id: $id")
    return PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_MAIN_ACTIVITY
            it.putExtra(Constants.EXTRA_WORKOUT_ID, id)
            //Set data uri to deeplink uri -> automatically navigates when navGraph is created
            it.data = Uri.parse(WORKOUT_DETAIL_URI + "$id")
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}

fun getTimeFromWorkoutState(
    wasPaused: Boolean,
    state: WorkoutState,
    currentTime: Long,
    workout: Workout
): Long {
    return if (wasPaused) currentTime
    else {
        when (state) {
            WorkoutState.STARTING -> Constants.TIMER_STARTING_IN_TIME
            WorkoutState.BREAK -> workout.pauseTime
            else -> workout.exerciseTime
        }
    }
}
