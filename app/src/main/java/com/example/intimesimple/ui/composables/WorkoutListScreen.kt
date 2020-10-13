package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.intimesimple.R
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.getRandomWorkout
import timber.log.Timber

@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    workoutListViewModel: WorkoutListViewModel
){
    // get workout list as observable state
    var showDialog by remember { mutableStateOf(false) }
    val workouts by workoutListViewModel.workouts.observeAsState(listOf())


    // build screen layout with scaffold
    Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    text = stringResource(id = R.string.app_name).toUpperCase()
                            )
                        }
                )
            },
            bodyContent = {
                if (showDialog) {
                    WorkoutAddAlertDialog(
                            onAccept = {
                                showDialog = false
                            },
                            onDismiss = {
                                showDialog = false
                            },
                            workoutListViewModel = workoutListViewModel
                    )
                }
                WorkoutListBodyContent(
                        Modifier.fillMaxSize(),
                        workoutListViewModel = workoutListViewModel,
                        items = workouts,
                        navigateToDetail = navigateToDetail
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = { showDialog = true },
                        icon = { Icon(Icons.Filled.Add) },
                        backgroundColor = Green500
                )
            },
            floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun WorkoutListBodyContent(
    modifier: Modifier = Modifier,
    workoutListViewModel: WorkoutListViewModel,
    items: List<Workout>,
    navigateToDetail: (Long) -> Unit
) {
    LazyColumnFor(
            items = items,
            contentPadding = PaddingValues(4.dp)
    ) { item ->
        WorkoutItem(
                workout = item,
                onClick = {
                    workoutListViewModel.deleteWorkoutWithId(it.id)
                }
        )

    }
}

@Composable
private fun AddWorkoutFab(onAddItem: (Workout) -> Unit) {
    FloatingActionButton(
        onClick = { onAddItem(getRandomWorkout()) },
        icon = { Icon(Icons.Filled.Add) },
            backgroundColor = Green500
    )
}