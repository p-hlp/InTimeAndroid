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

@ExperimentalMaterialApi
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    workoutListViewModel: WorkoutListViewModel
){
    // get workout list as observable state
    val workouts by workoutListViewModel.workouts.observeAsState(listOf())
    val screenState by workoutListViewModel.workoutListScreenState.observeAsState()

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

                when(screenState){
                    WorkoutListViewModel.WorkoutListScreenState.List -> {
                        WorkoutListContent(
                                innerPadding = PaddingValues(4.dp),
                                items = workouts,
                                onSwipe = {
                                    workoutListViewModel.deleteWorkout(it)
                                },
                                onClick = {
                                    navigateToDetail(it.id)
                                }
                        )
                    }
                    WorkoutListViewModel.WorkoutListScreenState.AddItem -> {
                        // TODO: Show add item screen here
                    }
                }

            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = { workoutListViewModel.setScreenState(
                                WorkoutListViewModel.WorkoutListScreenState.AddItem)
                        },
                        icon = { Icon(Icons.Filled.Add) },
                        backgroundColor = Green500
                )
            },
            floatingActionButtonPosition = FabPosition.End
    )
}
