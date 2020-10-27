package com.example.intimesimple.ui.composables.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.*
import androidx.navigation.navDeepLink
import com.example.intimesimple.ui.composables.WorkoutAddScreen
import com.example.intimesimple.ui.composables.WorkoutDetailScreen
import com.example.intimesimple.ui.composables.WorkoutListScreen
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.WORKOUT_DETAIL_URI

@ExperimentalMaterialApi
@Composable
fun AppNavigation(
        navController: NavHostController,
        workoutListViewModel: WorkoutListViewModel,
        workoutDetailViewModel: WorkoutDetailViewModel,
        sendServiceCommand: (String) -> Unit
) {
    NavHost(navController, startDestination = Screen.WorkoutListScreen.route) {
        // NavGraph
        composable(Screen.WorkoutListScreen.route) {
            WorkoutListScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutListViewModel = workoutListViewModel,
                    sendServiceCommand = sendServiceCommand
            )
        }
        composable(Screen.WorkoutAddScreen.route) {
            WorkoutAddScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutListViewModel = workoutListViewModel
            )
        }
        composable(
                Screen.WorkoutDetailScreen.route,
                arguments = listOf(navArgument("id"){ defaultValue = -1L}),
                deepLinks = listOf(navDeepLink { uriPattern = "$WORKOUT_DETAIL_URI{id}"})
        ) {
            WorkoutDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutDetailViewModel = workoutDetailViewModel,
                    sendServiceCommand = sendServiceCommand,
                    workoutId = it.arguments?.get("id") as? Long
            )
        }
    }
}
