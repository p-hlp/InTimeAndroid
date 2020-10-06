package com.example.intimesimple.utils

import java.text.DateFormat.getDateTimeInstance
import java.util.*

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = getDateTimeInstance()
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    val df = getDateTimeInstance()
    df.parse(date)?.let {
        return it.time
    }
    return 0L
}