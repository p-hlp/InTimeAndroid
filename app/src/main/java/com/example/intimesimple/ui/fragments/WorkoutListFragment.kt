package com.example.intimesimple.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
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
                    WorkoutListScreen()
                }
            }
        }
    }
}