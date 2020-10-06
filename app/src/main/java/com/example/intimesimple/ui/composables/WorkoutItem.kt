package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.defaultWorkouts
import com.example.intimesimple.utils.convertLongToTime
import com.example.intimesimple.utils.getFormattedCompletionTime

@Composable
fun WorkoutItem(workout: Workout, modifier: Modifier = Modifier){
    Card(
            modifier = modifier
                    .clickable(onClick = {/* navigate to detailscreen */})
                    .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
                modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                    Modifier.weight(1f)
            ){
                WorkoutCardInfoColumn(workout)
            }
            Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
            ){
                WorkoutCardTimeColumn(workout)
            }
        }
    }
}


@Preview
@Composable
fun WorkoutItemPreview(){
    val testWorkout =
            Workout(0,"15min Posture", 35000L, 15000L, 18)
    MaterialTheme {
        WorkoutItem(testWorkout)
    }
}


@Composable
fun WorkoutCardInfoColumn(workout: Workout){
    // Name, last completion
    Text(workout.name, style = typography.subtitle1, maxLines = 2, fontWeight = FontWeight.SemiBold)
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text("${workout.repetitions} Exercises", style = typography.body2)
    }
    Spacer(modifier = Modifier.padding(4.dp))
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text("last completion", style = typography.body2)
    }
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text(
                convertLongToTime(workout.lastCompletion),
                style = typography.body2,
                fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun WorkoutCardTimeColumn(workout: Workout){
    Surface(
            color = MaterialTheme.colors.secondary.copy(alpha = 0.3f),
            shape = RoundedCornerShape(4.dp)
    ){
        Text(
                getFormattedCompletionTime(workout.repetitions * workout.exerciseTime),
                style = typography.subtitle1,
                modifier = Modifier.padding(8.dp)
        )
    }
}