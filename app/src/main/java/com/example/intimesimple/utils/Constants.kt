package com.example.intimesimple.utils

object Constants {
    const val DATABASE_NAME             = "app_db"

    // Intent Actions for communication with timer service
    const val ACTION_START              = "ACTION_START"
    const val ACTION_RESUME             = "ACTION_RESUME"
    const val ACTION_PAUSE              = "ACTION_PAUSE"
    const val ACTION_CANCEL             = "ACTION_CANCEL"
    const val ACTION_NEXT               = "ACTION_NEXT"
    const val ACTION_PREVIOUS           = "ACTION_PREVIOUS"
    const val ACTION_SHOW_MAIN_ACTIVITY = "ACTION_SHOW_MAIN_ACTIVITY"

    const val EXTRA_REPETITION          = "EXTRA_REPETITION"
    const val EXTRA_EXERCISETIME        = "EXTRA_EXERCISETIME"
    const val EXTRA_PAUSETIME           = "EXTRA_PAUSETIME"
    const val EXTRA_WORKOUT_ID          = "EXTRA_WORKOUT_ID"

    const val NOTIFICATION_CHANNEL_ID   = "timer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Timer"
    const val NOTIFICATION_ID           = 1

    const val ONE_SECOND                = 1000L
    const val TIMER_UPDATE_INTERVAL     = 5L    //5ms
    const val TIMER_STARTING_IN_TIME    = 5000L //5s
}