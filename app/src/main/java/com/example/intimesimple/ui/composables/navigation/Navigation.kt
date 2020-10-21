package com.example.intimesimple.ui.composables.navigation

import android.os.Bundle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.intimesimple.ui.composables.WorkoutAddScreen
import com.example.intimesimple.ui.composables.WorkoutDetailScreen
import com.example.intimesimple.ui.composables.WorkoutListScreen
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID

@ExperimentalMaterialApi
@Composable
fun AppNavigation(
        navController: NavHostController,
        workoutListViewModel: WorkoutListViewModel,
        workoutDetailViewModel: WorkoutDetailViewModel,
        sendServiceCommand: (String) -> Unit
) {
    val currentScreen by navController.currentBackStackEntryAsState()

    NavHost(navController, startDestination = Screen.WorkoutListScreen) {
        // NavGraph
        composable(Screen.WorkoutListScreen) {
            WorkoutListScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutListViewModel = workoutListViewModel
            )
        }
        composable(Screen.WorkoutAddScreen) {
            WorkoutAddScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutListViewModel = workoutListViewModel
            )
        }
        composable(Screen.WorkoutDetailScreen) {
            WorkoutDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    workoutDetailViewModel = workoutDetailViewModel,
                    sendServiceCommand = sendServiceCommand
            )
        }
    }
}


@Composable
fun NavigateButton(
        navController: NavController,
        screen: Screen,
        args: Bundle? = null,
        text: String = screen.title
) {
    Button(
            onClick = { navController.navigate(screen.title, args) },
            backgroundColor = LightGray,
            modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Navigate to $text")
    }
}

@Composable
fun NavigateBackButton(navController: NavController, modifier: Modifier = Modifier) {
    if (navController.previousBackStackEntry != null) {
        Button(
                onClick = { navController.popBackStack() },
                backgroundColor = LightGray,
                modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Previous screen")
        }
    }
}
