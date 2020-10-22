package com.example.intimesimple.ui.composables

import androidx.navigation.compose.*
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
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
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.composables.navigation.Screen
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.EXTRA_WORKOUT_ID
import timber.log.Timber


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
            bodyContent = { paddingValues ->
                WorkoutListContent(
                        modifier = modifier.padding(paddingValues),
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

@ExperimentalMaterialApi
@Composable
fun WorkoutListContent(
        modifier: Modifier = Modifier,
        innerPadding: PaddingValues,
        items: List<Workout>,
        onSwipe: (Workout) -> Unit,
        onClick: (Workout) -> Unit
) {
    LazyColumnFor(
            modifier = modifier.padding(innerPadding),
            items = items,
    ) { item ->
        // https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key
        key(item.id){
            val dismissState = rememberDismissState()
            //Timber.d("DismissState: ${dismissState.value}")
            onCommit(dismissState.value){
                if(dismissState.value == DismissValue.DismissedToEnd ){
                    //Timber.d("onSwipe() - Dismissing WorkoutID: ${item.id}")
                    onSwipe(item)
                }
            }

            // Fixed with alpha05
            SwipeToDismiss(
                    modifier = modifier,
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd),
                    background = {
                        WorkoutItemDismissBackground()
                    }
            ){
                WorkoutItem(
                        workout = item,
                        onClick = onClick
                )
            }
        }
    }
}

@Composable
fun WorkoutListAnimatedContent(
        modifier: Modifier = Modifier,
        innerPadding: PaddingValues,
        items: List<Workout>,
        onSwipe: (Workout) -> Unit,
        onClick: (Workout) -> Unit
){
    LazyColumnFor(
            modifier = modifier.padding(innerPadding),
            items = items,
    ) { item ->
        // https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key
        key(item.id){
            AnimatedSwipeDismiss(
                    item = item,
                    background = {
                        WorkoutItemDismissBackground()
                    },
                    content = {
                        WorkoutItem(
                                workout = item,
                                onClick = onClick
                        )
                    },
                    onDismiss = {
                        onSwipe(item)
                    }
            )
        }
    }
}