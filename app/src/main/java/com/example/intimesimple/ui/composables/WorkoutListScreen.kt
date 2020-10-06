package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.defaultWorkouts
import com.example.intimesimple.data.local.getRandomWorkout
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel

@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier
){
    // get workout list as observable state
    val workoutListViewModel: WorkoutListViewModel = viewModel()

    // build screen layout with scaffold
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(title = "INTime")
        },
        bodyContent = {
            WorkoutBodyContent(items = defaultWorkouts, navigateTo = {})
        },
        floatingActionButton = {
            AddWorkoutFab(onAddItem = {})
        },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun WorkoutBodyContent(
    items: List<Workout>,
    navigateTo: () -> Unit
) {
    LazyColumnFor(
        items = items,
        contentPadding = PaddingValues(top = 4.dp)
    ) { item ->
        WorkoutItem(
            workout = item
        )
    }
}

@Preview
@Composable
fun WorkoutBodyContentPreview(){
    MaterialTheme {
        WorkoutBodyContent(items = defaultWorkouts, navigateTo = {})
    }
}

@Composable
private fun AddWorkoutFab(onAddItem: (Workout) -> Unit) {
    FloatingActionButton(
        onClick = { onAddItem(getRandomWorkout()) },
        icon = { Icon(Icons.Filled.Add) }
    )
}