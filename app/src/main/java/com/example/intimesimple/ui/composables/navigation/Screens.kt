package com.example.intimesimple.ui.composables.navigation

import androidx.annotation.StringRes
import com.example.intimesimple.R

sealed class Screen(val route: String, @StringRes val resourceId: Int){
    object WorkoutListScreen: Screen("workoutlist", R.string.workoutlist)
    object WorkoutAddScreen: Screen("workoutadd", R.string.workoutadd)
    object WorkoutDetailScreen: Screen("workoutdetail", R.string.workoutdetail)
}