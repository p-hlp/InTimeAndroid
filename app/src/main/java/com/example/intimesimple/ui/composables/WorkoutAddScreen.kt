package com.example.intimesimple.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.intimesimple.R
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.getRandomWorkout

@Composable
fun WorkoutAddScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        workoutListViewModel: WorkoutListViewModel
){
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
                WorkoutAddScreenContent(
                        modifier = modifier.padding(it),
                        navController = navController,
                        workoutListViewModel = workoutListViewModel
                )
            }
    )
}

//TODO: Add proper layout for adding a workout
@Composable
fun WorkoutAddScreenContent(
        modifier: Modifier = Modifier,
        navController: NavController,
        workoutListViewModel: WorkoutListViewModel
){
    ConstraintLayout(
            modifier = modifier
    ) {
        val buttonRow = createRef()

        Row(
                modifier = Modifier.constrainAs(buttonRow){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            TextButton(
                    onClick = {
                        // Add random workout for testing purposes
                        workoutListViewModel.addWorkout(getRandomWorkout())
                        // Pop backstack -> back to list screen
                        navController.popBackStack()
                    },
                    border = BorderStroke(1.dp, Green500)
            ) {
                Text(text = "Add")
            }

            TextButton(
                    onClick = {
                        // Pop backstack -> back to list screen
                        navController.popBackStack()
                    },
                    border = BorderStroke(1.dp, Green500)
            ) {
                Text(text = "Cancel")
            }

        }
    }
}