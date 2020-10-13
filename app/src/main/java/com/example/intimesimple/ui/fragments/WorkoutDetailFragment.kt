package com.example.intimesimple.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.composables.WorkoutDetailScreen
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailFragment : Fragment() {

    private val args: WorkoutDetailFragmentArgs by navArgs()
    private val workoutDetailViewModel: WorkoutDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                INTimeTheme{
                    WorkoutDetailScreen(
                            Modifier.fillMaxSize(),
                            ::sendCommandToTestService,
                            ::navigateHome,
                            workoutDetailViewModel
                    )
                }
            }
        }
    }


    private fun navigateHome() {
        sendCommandToTestService(ACTION_CANCEL)
        findNavController().navigate(WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToWorkoutListFragment())
    }

    private fun sendCommandToTestService(action: String){
        Intent(context, TimerService::class.java).also {
            it.action = action
            if (action == ACTION_START) {
                it.putExtra(EXTRA_WORKOUT_ID, args.wId)
            }
            context?.startService(it)
        }
    }
}