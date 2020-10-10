package com.example.intimesimple.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.intimesimple.ui.composables.WorkoutListScreen
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutListFragment : Fragment(){

    private val workoutListViewModel: WorkoutListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                INTimeTheme {
                    WorkoutListScreen(
                        navigateToDetail = ::navigateToDetail,
                            workoutListViewModel = workoutListViewModel
                    )
                }
            }
        }
    }

    private fun navigateToDetail(id: Long){
        findNavController().navigate(WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment(id))
    }
}