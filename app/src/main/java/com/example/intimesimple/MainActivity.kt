package com.example.intimesimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.intimesimple.ui.fragments.WorkoutListFragmentDirections
import com.example.intimesimple.utils.Constants.ACTION_SHOW_MAIN_ACTIVITY
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        Timber.d("onCreate")
        navigateToAfterServiceNotificationPressed(intent)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToAfterServiceNotificationPressed(intent)
    }

    private fun navigateToAfterServiceNotificationPressed(intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_SHOW_MAIN_ACTIVITY) {
                // Get id and navigate to DetailFragment
                val id = it.getLongExtra(EXTRA_WORKOUT_ID, -1L)
                if(id != -1L){
                    Timber.d("Pressed Notification: navigating to: $id")
                    if(navHostFragment.navController.currentDestination?.id == R.id.workoutListFragment){
                        navHostFragment.navController.navigate(WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment(id))
                    }

                }
            }
        }
    }
}