package com.example.intimesimple.utils

import java.util.concurrent.TimeUnit

fun getFormattedStopWatchTime(ms: Long): String{
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

fun getFormattedCompletionTime(ms: Long): String{
    var milliseconds = ms
    // Convert to hours
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    milliseconds -= TimeUnit.HOURS.toMillis(hours)

    // Convert to minutes
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

    // Convert to seconds
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

    return (if(hours < 10) "" else "$hours:") +
            (if(minutes < 10) "" else "$minutes:") +
            (if(seconds < 10) "" else "$seconds") +
            if(hours > 0) " h" else if(minutes > 0) " min" else "sec"
}