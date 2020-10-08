package com.example.intimesimple.services

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleService
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TestService : LifecycleService(){

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            Timber.d("Action: ${it.action}")
        }
        return super.onStartCommand(intent, flags, startId)
    }
}