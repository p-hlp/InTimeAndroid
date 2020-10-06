package com.example.intimesimple.data.local

val defaultWorkouts = listOf(
        Workout(0,"15min Posture", 35000L, 15000L, 18),
        Workout(1,"Morning Yoga", 30000L, 15000L, 12),
        Workout(2,"Upper Body", 40000L, 20000L, 8),
        Workout(3,"Lower Body", 40000L, 20000L, 8),
        Workout(4,"Core Routine", 35000L, 25000L, 9),
        Workout(5,"Conditioning", 50000L, 20000L, 5),
        Workout(6,"15min Posture", 35000L, 15000L, 18),
        Workout(7,"Morning Yoga", 30000L, 15000L, 12),
        Workout(8,"Upper Body", 40000L, 20000L, 8),
        Workout(9,"Lower Body", 40000L, 20000L, 8),
        Workout(10,"Core Routine", 35000L, 25000L, 9),
        Workout(11,"Conditioning", 50000L, 20000L, 5),
)

fun getRandomWorkout() = defaultWorkouts.random()