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
import com.example.intimesimple.utils.getCompletionTimeForWorkout
import com.example.intimesimple.utils.getFormattedCompletionTime

@Composable
fun WorkoutItem(
    modifier: Modifier = Modifier,
    workout: Workout,
    onClick: (Workout) -> Unit
){
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = {onClick(workout)})
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
fun WorkoutItemDismissBackground(){
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium,
        color = Green500.copy(alpha = 0.75f)
    ){}
}

@Composable
fun WorkoutCardInfoColumn(workout: Workout){
    // Name, last completion
    val emphasisLevels = AmbientEmphasisLevels.current
    Text(workout.name, style = MaterialTheme.typography.h3, maxLines = 2)

    Spacer(modifier = Modifier.padding(4.dp))

    ProvideEmphasis(emphasisLevels.medium) {
        Text("${workout.repetitions} Repetition${if(workout.repetitions > 1) "s" else ""}", style = MaterialTheme.typography.body2)

        Text("${getFormattedCompletionTime(workout.exerciseTime)} Work", style = MaterialTheme.typography.body2)

        Text("${getFormattedCompletionTime(workout.pauseTime)} Pause", style = MaterialTheme.typography.body2)
    }
}


@Composable
fun WorkoutCardTimeColumn(workout: Workout){
    Surface(
        color = Green500.copy(alpha = 0.75f),
        shape = RoundedCornerShape(4.dp)
    ){
        Text(text = getCompletionTimeForWorkout(workout),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(8.dp)
        )
    }
}