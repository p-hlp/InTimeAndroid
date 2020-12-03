package com.example.intimesimple.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.intimesimple.R
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.utils.Constants.ONE_SECOND
import com.example.intimesimple.utils.getFormattedStopWatchTime
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun WorkoutAddScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        workoutListViewModel: WorkoutListViewModel
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
            snackbarHost = {
                // reuse default SnackbarHost to have default animation and timing handling
                SnackbarHost(it) { data ->
                    Snackbar(
                            snackbarData = data,
                    )
                }
            },
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    text = stringResource(id = R.string.workoutAddTitle)
                                            .toUpperCase()
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                    onClick = {
                                        // navigate back
                                        navController.popBackStack()
                                    },
                                    content = {
                                        Icon(Icons.Filled.ArrowBack)
                                    }
                            )
                        }
                )
            },
            bodyContent = {
                WorkoutAddScreenContent(
                        modifier = modifier.padding(it),
                        navController = navController,
                        workoutListViewModel = workoutListViewModel,
                        scaffoldState = scaffoldState
                )
            }
    )
}

@ExperimentalMaterialApi
@Composable
fun WorkoutAddScreenContent(
        modifier: Modifier = Modifier,
        navController: NavController,
        workoutListViewModel: WorkoutListViewModel,
        scaffoldState: ScaffoldState
){
    val scope = rememberCoroutineScope()
    val configuration = androidx.compose.ui.platform.ConfigurationAmbient.current
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = 0.3f * screenWidth
    val rowWidth = .75f * screenWidth

    ConstraintLayout(
            modifier = modifier
    ) {
        val (buttonRow, textFieldRow, workRow, pauseRow, repsRow) = createRefs()
        var nameField by savedInstanceState {""}
        var workField by savedInstanceState {30000L}
        var pauseField by savedInstanceState {10000L}
        var repsField by savedInstanceState {1}
        var errorState by remember { mutableStateOf(false) }
        val invalidInput = nameField.isBlank()

        OutlinedTextField(
                modifier = Modifier.width(rowWidth.dp).constrainAs(textFieldRow) {
                    top.linkTo(parent.top, 32.dp)
                    centerHorizontallyTo(parent)
                },
                value = nameField,
                onValueChange = {
                    if(!it.isBlank()){
                        errorState = false
                    }
                    nameField = it
                },
                label = { Text("Name") },
                placeholder = { Text("Enter name here") },
                textStyle = typography.body1,
                isErrorValue = errorState
        )

        clickInputField(
                modifier = Modifier.width(rowWidth.dp).constrainAs(workRow){
                    top.linkTo(textFieldRow.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                },
                label = "Work",
                content = {
                    Text(getFormattedStopWatchTime(workField),style = typography.h3)
                },
                onMinusClicked = {
                    if(workField >= 2000L){
                        workField -= ONE_SECOND
                    }
                },
                onPlusClicked = {
                    workField += ONE_SECOND
                }
        )

        clickInputField(
                modifier = Modifier.width(rowWidth.dp).constrainAs(pauseRow){
                    top.linkTo(workRow.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                },
                label = "Pause",
                content = {
                    Text(getFormattedStopWatchTime(pauseField),style = typography.h3)
                },
                onMinusClicked = {
                    if(pauseField > ONE_SECOND){
                        pauseField -= ONE_SECOND
                    }
                },
                onPlusClicked = {
                    pauseField += ONE_SECOND
                }
        )

        clickInputField(
                modifier = Modifier.width(rowWidth.dp).constrainAs(repsRow){
                    top.linkTo(pauseRow.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                },
                label = "Reps",
                content = {
                    Text(repsField.toString(),style = typography.h3)
                },
                onMinusClicked = {
                    if(repsField > 1){
                        repsField -= 1
                    }
                },
                onPlusClicked = {
                    repsField += 1
                }
        )
        Row(
                modifier = Modifier.constrainAs(buttonRow){
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, 32.dp)
                }.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                    modifier = Modifier.width(buttonWidth.dp),
                    onClick = {
                        if (!invalidInput) {
                            // build workout from inputs
                            val workout = Workout(
                                    name = nameField,
                                    exerciseTime = workField,
                                    pauseTime = pauseField,
                                    repetitions = repsField
                            )
                            // insert into db
                            workoutListViewModel.addWorkout(workout)
                            // Pop backstack -> back to list screen
                            navController.popBackStack()
                        } else {
                            errorState = true
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                        "Name-Field is empty, please enter a valid name."
                                )
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Add")
            }
            Button(
                    modifier = Modifier.width(buttonWidth.dp),
                    onClick = {
                        // Pop backstack -> back to list screen
                        navController.popBackStack()
                    },
                    shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Cancel")
            }

        }
    }
}


@Composable
fun clickInputField(
        modifier: Modifier = Modifier,
        label: String,
        content: @Composable () -> Unit,
        onMinusClicked: () -> Unit,
        onPlusClicked: () -> Unit
){
    val emphasisLevels = AmbientEmphasisLevels.current

    Column(
            modifier
    ){

        ProvideEmphasis(emphasisLevels.disabled){
            Text(
                text = label,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 2.dp),
                style = typography.subtitle2
            )
        }

        Card(
                modifier = Modifier.height(50.dp),
                elevation = 1.dp,
        ){
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (minusFab, plusFab, textBox) = createRefs()

                IconButton(
                        modifier = Modifier.constrainAs(minusFab) {
                            start.linkTo(parent.start)
                            centerVerticallyTo(parent)
                        },
                        onClick = {onMinusClicked()},
                        content = {Icon(Icons.Filled.Remove)}
                )

                Box(
                        modifier = Modifier.constrainAs(textBox){
                            centerVerticallyTo(parent)
                            centerHorizontallyTo(parent)
                        }
                ){
                    content()
                }

                IconButton(
                        modifier = Modifier. constrainAs(plusFab){
                            end.linkTo(parent.end)
                            centerVerticallyTo(parent)
                        },
                        onClick = {onPlusClicked()},
                    content = {Icon(Icons.Filled.Add)}
                )
            }
        }
    }

}