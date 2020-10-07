package com.example.intimesimple.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.intimesimple.repositories.WorkoutRepository

class WorkoutDetailViewModel @ViewModelInject constructor(
    val workoutRepository: WorkoutRepository
) : ViewModel(){

}