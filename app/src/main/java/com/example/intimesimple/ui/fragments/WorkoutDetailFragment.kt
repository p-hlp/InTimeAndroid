package com.example.intimesimple.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.composables.WorkoutDetailScreen
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.Constants.EXTRA_EXERCISETIME
import com.example.intimesimple.utils.Constants.EXTRA_PAUSETIME
import com.example.intimesimple.utils.Constants.EXTRA_REPETITION
import timber.log.Timber

class WorkoutDetailFragment : Fragment() {

    private val args: WorkoutDetailFragmentArgs by navArgs()
    // TODO: Init UI with current exercise time when entering this fragment
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
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
            // If starting service pass needed information in extra
            if(action == ACTION_START){
                it.putExtra(EXTRA_REPETITION, 3)
                it.putExtra(EXTRA_EXERCISETIME, 15000L)
                it.putExtra(EXTRA_PAUSETIME, 5000L)
            }
            context?.startService(it)
        }
    }
}