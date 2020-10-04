package com.example.intimesimple.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "exercise_time") var exerciseTime: Long,
    @ColumnInfo(name = "pause_time") var pauseTime: Long,
    @ColumnInfo(name = "repetitions") var repetitions: Int,
    @ColumnInfo(name = "created_at") var createdAt: Long
)