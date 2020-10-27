package com.example.intimesimple

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavHostController
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import androidx.navigation.compose.rememberNavController
import com.example.intimesimple.services.TestService
import com.example.intimesimple.ui.composables.navigation.AppNavigation
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.ACTION_INITIALIZE_DATA
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val workoutDetailViewModel: WorkoutDetailViewModel by viewModels()
    private val workoutListViewModel: WorkoutListViewModel by viewModels()
    private lateinit var navHostController: NavHostController

    private var bound: Boolean = false

    /*This acts as a dummy to trigger onBind/onRebind/onUnbind in TimerService*/
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {}
        override fun onServiceDisconnected(className: ComponentName) {}
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            INTimeTheme {
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

    override fun onStart() {
        super.onStart()
        //Timber.d("onStart")
        // Bind to the service
//        Intent(this, TimerService::class.java).also { intent ->
//            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
//        }
        Intent(this, TestService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
        bound = true
    }

    override fun onStop() {
        super.onStop()
        //Timber.d("onStop")
        // Unbind from the service
        if (bound) {
            //Timber.d("Trying to unbind service")
            unbindService(mConnection)
            bound = false
        }
    }

    /*Navigation from service notification is now handled with compose-navigation deep links
    * pendingIntent has a data uri that is the same as destination deeplink in NavigationGraph*/
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    private fun sendCommandToService(action: String) {
        Intent(this, TestService::class.java).also {
            it.action = action
            val id = navHostController.currentBackStackEntry?.arguments?.get("id") as? Long
            Timber.d("sendCommandService - Action: $action - ID: $id")
            if (action == ACTION_INITIALIZE_DATA) {
                it.putExtra(EXTRA_WORKOUT_ID, id)
            }
            startService(it)
        }
    }
}