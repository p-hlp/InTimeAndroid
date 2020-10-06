package com.example.intimesimple.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "exercise_time") var exerciseTime: Long = 45000, // default 45s
    @ColumnInfo(name = "pause_time") var pauseTime: Long = 15000L,      // default 15s
    @ColumnInfo(name = "repetitions") var repetitions: Int,
    @ColumnInfo(name = "last_completion") var lastCompletion: Long,     // Test values
    @ColumnInfo(name = "created_at") var createdAt: Long
)