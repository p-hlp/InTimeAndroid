package com.example.intimesimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavHostController
import com.example.intimesimple.utils.Constants.ACTION_SHOW_MAIN_ACTIVITY
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import androidx.navigation.compose.rememberNavController
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.composables.navigation.AppNavigation
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.ACTION_START
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val workoutDetailViewModel: WorkoutDetailViewModel by viewModels()
    private val workoutListViewModel: WorkoutListViewModel by viewModels()
    private lateinit var navHostController: NavHostController

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            INTimeTheme {
                /*TODO: handle case when activity is created from notification and there's
                *  no current navigation node - can only navigate after navGraph has been build*/
                navHostController = rememberNavController()
                AppNavigation(
                        navController = navHostController,
                        workoutListViewModel = workoutListViewModel,
                        workoutDetailViewModel = workoutDetailViewModel,
                        sendServiceCommand = ::sendCommandToService
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    // TODO: Currently not being called when tapping notification
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent")
        navigateToAfterServiceNotificationPressed(intent)
    }


    private fun navigateToAfterServiceNotificationPressed(intent: Intent?) {
        intent?.let {
            Timber.d("navigateToAfterServiceNotificationPressed - action: ${it.action}")
            if (it.action == ACTION_SHOW_MAIN_ACTIVITY) {
                // Get id and navigate to DetailFragment
                val id = it.getLongExtra(EXTRA_WORKOUT_ID, -1L)
                if (id != -1L) {
//                    navHostController.navigate(Screen.WorkoutDetailScreen, bundleOf(EXTRA_WORKOUT_ID to id))
                }
            }
        }
    }


    private fun sendCommandToService(action: String) {
        Intent(this, TimerService::class.java).also {
            it.action = action
            val id = navHostController.currentBackStackEntry?.arguments?.getLong(EXTRA_WORKOUT_ID)
            Timber.d("sendCommandService - Action: $action - ID: $id")
            if (action == ACTION_START) {
                it.putExtra(EXTRA_WORKOUT_ID, id)
            }
            startService(it)
        }
    }
}