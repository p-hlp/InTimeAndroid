package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.utils.convertLongToTime
import com.example.intimesimple.utils.getFormattedCompletionTime

@Composable
fun WorkoutItem(
        workout: Workout,
        modifier: Modifier = Modifier,
        navigateToDetail: (Long) -> Unit
){
    Card(
            modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable(onClick = {navigateToDetail(workout.id)})
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


@Composable
fun WorkoutCardInfoColumn(workout: Workout){
    // Name, last completion
    Text(workout.name, style = MaterialTheme.typography.h3, maxLines = 2)
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text("${workout.repetitions} Repetitions", style = MaterialTheme.typography.body2)
    }
    Spacer(modifier = Modifier.padding(4.dp))
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text("last completion", style = MaterialTheme.typography.body2)
    }
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium) {
        Text(
                convertLongToTime(workout.lastCompletion),
                style = MaterialTheme.typography.body2
        )
    }
}


@Composable
fun WorkoutCardTimeColumn(workout: Workout){
    Surface(
            color = Green500.copy(alpha = 0.75f),
            shape = RoundedCornerShape(4.dp)
    ){
        Text(
                getFormattedCompletionTime(workout.repetitions * workout.exerciseTime),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(8.dp)
        )
    }
}