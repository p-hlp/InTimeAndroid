package com.example.intimesimple.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_NEXT
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_PREVIOUS
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TimerService: LifecycleService(){

    companion object {

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // handle actions
        intent?.let {
            when(it.action){
                ACTION_START    -> {
                    Timber.d("ACTION_START")
                }

                ACTION_RESUME   -> {
                    Timber.d("ACTION_RESUME")
                }

                ACTION_PAUSE    -> {
                    Timber.d("ACTION_PAUSE")
                }

                ACTION_CANCEL   -> {
                    Timber.d("ACTION_CANCEL")
                }

                ACTION_NEXT     -> {
                    Timber.d("ACTION_NEXT")
                }

                ACTION_PREVIOUS -> {
                    Timber.d("ACTION_PREVIOUS")
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }
}