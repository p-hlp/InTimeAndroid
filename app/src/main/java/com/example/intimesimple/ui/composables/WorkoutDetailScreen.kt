package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WorkoutDetailScreen(modifier: Modifier = Modifier){
    Box {
        Text(
            modifier = Modifier.fillMaxSize()
                .align(Alignment.Center),
            text = "This is WorkoutDetailScreen",
        )
    }
}


