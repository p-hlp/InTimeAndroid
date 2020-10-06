package com.example.intimesimple.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.intimesimple.ui.composables.WorkoutListScreen
import com.example.intimesimple.ui.theme.INTimeTheme

class WorkoutListFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                INTimeTheme {
                    WorkoutListScreen(
                        navigateToDetail = ::navigateToDetail
                    )
                }
            }
        }
    }

    private fun navigateToDetail(id: Long){
        findNavController().navigate(WorkoutListFragmentDirections.actionWorkoutListFragmentToWorkoutDetailFragment(id))
    }
}