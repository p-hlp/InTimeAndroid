package com.example.intimesimple.ui.composables

import androidx.navigation.compose.*
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.intimesimple.R
import com.example.intimesimple.ui.composables.navigation.Screen
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID


@ExperimentalMaterialApi
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    workoutListViewModel: WorkoutListViewModel
){
    // get workout list as observable state
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
                WorkoutListContent(
                        innerPadding = PaddingValues(4.dp),
                        items = workouts,
                        onSwipe = {
                            workoutListViewModel.deleteWorkout(it)
                        },
                        onClick = {
                            navController.navigate(
                                    Screen.WorkoutDetailScreen,
                                    bundleOf(EXTRA_WORKOUT_ID to it.id)
                            )
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.WorkoutAddScreen)
                        },
                        icon = {Icon(Icons.Filled.Add)},
                        backgroundColor = Green500
                )
            },
            floatingActionButtonPosition = FabPosition.End
    )
}
