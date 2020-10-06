package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.intimesimple.data.local.defaultWorkouts

@Composable
fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    navigateHome: () -> Unit,
    wId: Long
){
    // Get specific workout via viewmodel or whatever
   val workout = defaultWorkouts[wId.toInt()]



}


