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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.defaultWorkouts
import com.example.intimesimple.data.local.getRandomWorkout
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import java.util.*

@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit
){
    // get workout list as observable state
    var showDialog by remember { mutableStateOf(false) }
    val alertMessage = "This is a placeholder for alert dialog"

    // build screen layout with scaffold
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(title = "INTime")
        },
        bodyContent = {
            if (showDialog) {
                INTimeAlertDialog(
                    onAccept = {
                        // add workout
                        // dismiss dialog
                        showDialog = false
                    },
                    onDismiss = {
                        showDialog = false
                    },
                    bodyText = alertMessage,
                    buttonAcceptText = "add".toUpperCase(Locale.ROOT),
                    buttonDismissText = "cancel".toUpperCase(Locale.ROOT)

                )
            }
            WorkoutBodyContent(
                items = defaultWorkouts,
                navigateToDetail = navigateToDetail
            )
        },
        floatingActionButton = {
            AddWorkoutFab(onAddItem = {
                showDialog = true
            })
        },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun WorkoutBodyContent(
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
        WorkoutBodyContent(items = defaultWorkouts, navigateToDetail = {})
    }
}

@Composable
private fun AddWorkoutFab(onAddItem: (Workout) -> Unit) {
    FloatingActionButton(
        onClick = { onAddItem(getRandomWorkout()) },
        icon = { Icon(Icons.Filled.Add) }
    )
}