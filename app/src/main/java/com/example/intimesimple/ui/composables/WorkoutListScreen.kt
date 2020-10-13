package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.R
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.defaultWorkouts
import com.example.intimesimple.utils.getRandomWorkout
import java.util.*

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
                                // add workout
                                // dismiss dialog
                                showDialog = false
                            },
                            onDismiss = {
                                showDialog = false
                            },
                            bodyContent = {

                            },
                            buttonAcceptText = "add".toUpperCase(Locale.ROOT),
                            buttonDismissText = "cancel".toUpperCase(Locale.ROOT)

                    )
                }
                WorkoutListBodyContent(
                        Modifier.fillMaxSize(),
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
    items: List<Workout>,
    navigateToDetail: (Long) -> Unit
) {
    LazyColumnFor(
        items = items,
        contentPadding = PaddingValues(top = 4.dp)
    ) { item ->
        WorkoutItem(
            workout = item,
            navigateToDetail = navigateToDetail
        )
    }
}

@Preview
@Composable
fun WorkoutBodyContentPreview(){
    MaterialTheme {
        WorkoutListBodyContent(items = defaultWorkouts, navigateToDetail = {})
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