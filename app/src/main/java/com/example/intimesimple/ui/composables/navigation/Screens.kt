package com.example.intimesimple.ui.composables.navigation

sealed class Screen(val title: String){
    object WorkoutListScreen: Screen("WorkoutList")
    object WorkoutAddScreen: Screen("WorkoutAdd")
    object WorkoutDetailScreen: Screen("WorkoutDetail")
}