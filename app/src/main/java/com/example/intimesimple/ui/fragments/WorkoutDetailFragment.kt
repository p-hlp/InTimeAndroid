package com.example.intimesimple.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.composables.WorkoutDetailScreen
import com.example.intimesimple.ui.theme.INTimeTheme
import timber.log.Timber

class WorkoutDetailFragment : Fragment() {

    private val args: WorkoutDetailFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                INTimeTheme {
                    // Display WorkoutDetailScreen
                    WorkoutDetailScreen(
                            modifier = Modifier,
                            wId = args.wId,
                            navigateHome = ::navigateHome,
                            onServiceCommand = ::sendCommandToService
                    )
                }
            }
        }
    }

    private fun navigateHome() {
        findNavController().navigate(WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToWorkoutListFragment())
    }

    private fun sendCommandToService(action: String) {
        Intent(context, TimerService::class.java).also {
            it.action = action
            context?.startService(it)
            Timber.d("Command to service: ${it.action}")
        }
    }
}