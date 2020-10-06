package com.example.intimesimple.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.defaultWorkouts

@Composable
fun WorkoutItem(workout: Workout, modifier: Modifier = Modifier){
    
}


@Preview
@Composable
fun WorkoutItemPreview(){

    ThemedPreview(
            darkTheme = true,
            children = {
                WorkoutItem(workout = defaultWorkouts[0])
            }
    )
}