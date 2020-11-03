package com.example.intimesimple

import com.example.intimesimple.data.local.Workout

class TestUtil {

    companion object {
        val WORKOUT_1 = Workout(
            name = "Stretch",
            exerciseTime = 45000L,
            pauseTime = 15000L,
            repetitions = 10
        )

        val WORKOUT_2 = Workout(
            name = "Strength",
            exerciseTime = 60000L,
            pauseTime = 20000L,
            repetitions = 12
        )

        val WORKOUT_3 = Workout(
            name = "Abs",
            exerciseTime = 30000L,
            pauseTime = 15000L,
            repetitions = 6
        )
    }
}