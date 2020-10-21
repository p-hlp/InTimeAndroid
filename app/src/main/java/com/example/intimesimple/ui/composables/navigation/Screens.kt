package com.example.intimesimple.ui.composables.navigation

sealed class Screen(val title: String){
    object WorkoutListScreen: Screen("WorkoutList")
    object WorkoutAddScreen: Screen("WorkoutAdd")
    object WorkoutDetailScreen: Screen("WorkoutDetail")

    /**
     * hack to generate the same Destination ID that
     * the Compose Navigation lib generates
     **/
    val id: Int
        get() {
            return title.hashCode() + 0x00010000
        }
}