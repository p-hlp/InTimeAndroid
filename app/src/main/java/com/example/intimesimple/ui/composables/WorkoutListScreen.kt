package com.example.intimesimple.ui.composables

import androidx.compose.animation.Transition
import androidx.compose.animation.transition
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.R
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.animations.AnimationDefinitions
import com.example.intimesimple.ui.animations.AnimationDefinitions.explodeTransitionDefinition
import com.example.intimesimple.ui.animations.AnimationDefinitions.sizeState
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

                when (screenState) {
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
                        AddWorkoutContent(workoutListViewModel)
                    }
                }

            },
            floatingActionButton = {
                if (screenState == WorkoutListViewModel.WorkoutListScreenState.List) {
                    FloatingActionButton(
                            onClick = {
                                workoutListViewModel.setScreenState(
                                        WorkoutListViewModel.WorkoutListScreenState.AddItem
                                )
                            },
                            icon = {Icon(Icons.Filled.Add)},
                            backgroundColor = Green500
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
    )
}


@Composable
fun AddWorkoutContent(
        workoutListViewModel: WorkoutListViewModel
){
    Box(modifier = Modifier.fillMaxSize()){
        Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    workoutListViewModel.setScreenState(
                            WorkoutListViewModel.WorkoutListScreenState.List
                    )
                }
        ) {
            Text(text = "ToList")
        }
    }
}

@Composable
fun ExplodingFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    backGroundColor: Color
){
    var buttonState by remember { mutableStateOf(AnimationDefinitions.FabState.Idle) }

    val state = transition(
        definition = explodeTransitionDefinition(),
        initState = buttonState,
        toState = AnimationDefinitions.FabState.Exploded
    )

    FloatingActionButton(
        onClick = {
            onClick()
            buttonState = AnimationDefinitions.FabState.Exploded
        },
        modifier = Modifier.size(state[sizeState].dp),
        backgroundColor = backGroundColor,
        icon = icon
    )
}
