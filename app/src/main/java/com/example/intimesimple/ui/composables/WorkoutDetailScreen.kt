package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.getFormattedStopWatchTime
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.navigation.NavController
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.utils.Constants.TIMER_STARTING_IN_TIME
import timber.log.Timber


@Composable
fun WorkoutDetailScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        workoutDetailViewModel: WorkoutDetailViewModel,
        sendServiceCommand: (String) -> Unit,
        workoutId: Long? = null
) {
    val workout by workoutDetailViewModel.workout.observeAsState()

    onActive(callback = {
        // Do once on composition
        workoutDetailViewModel.setCurrentWorkout(workoutId)
    })

    Timber.d("WorkoutId: $workoutId - Workout: ${workout?.name ?: "null"}")
    Scaffold(
            modifier.fillMaxSize(),
            topBar = {
                workout?.let {
                    DetailScreenTopBar(
                            title = it.name,
                            navController = navController,
                            sendCommand = sendServiceCommand,
                            workoutDetailViewModel = workoutDetailViewModel
                    )
                }

            },
            bodyContent = { paddingValues ->
                workout?.let {
                    WorkoutDetailScreenContent(
                            modifier = modifier.padding(paddingValues),
                            sendCommand = sendServiceCommand,
                            workout = it,
                            workoutDetailViewModel = workoutDetailViewModel
                    )
                }
            }
    )

    onDispose(callback = {
        // Reset viewModel workout state when disposing composable
        workoutDetailViewModel.resetCurrentWorkout()
    })
}


// TODO: Refactor - state hoisting when possible
@Composable
fun WorkoutDetailScreenContent(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit,
        workout: Workout,
        workoutDetailViewModel: WorkoutDetailViewModel
) {
    val timerState by workoutDetailViewModel.timerState.observeAsState(TimerState.EXPIRED)
    val workoutState by workoutDetailViewModel.workoutState.observeAsState(WorkoutState.STARTING)
    val timeInMillis by workoutDetailViewModel.timeInMillis.observeAsState()
    val timerRepCount by workoutDetailViewModel.timerRepCount.observeAsState()
    val exTimeInMillis: Long = workout.exerciseTime
    val repCount: Int = workout.repetitions
    val progressTime by workoutDetailViewModel.progressTime.observeAsState(exTimeInMillis)
    val configuration = ConfigurationAmbient.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    val buttonWidth = 0.3f * screenWidthDp

    WithConstraints(modifier) {

        val constraints = if (screenWidthDp.dp < 600.dp) {
            portraitConstraints()
        } else landscapeConstraints()

        ConstraintLayout(modifier = modifier, constraintSet = constraints) {
            val buttonModifier = Modifier.width(buttonWidth.dp)

            TimerCircleComponent(
                    modifier = Modifier.layoutId("progCircle"),
                    screenWidthDp = screenWidthDp,
                    screenHeightDp = screenHeightDp,
                    time = (if (timerState == TimerState.EXPIRED)
                        getFormattedStopWatchTime(TIMER_STARTING_IN_TIME)
                    else getFormattedStopWatchTime(timeInMillis)),
                    state = workoutState.name,
                    reps = "${if (timerState == TimerState.EXPIRED) repCount else timerRepCount}",
                    elapsedTime =   if (timerState == TimerState.EXPIRED) TIMER_STARTING_IN_TIME
                    else progressTime,
                    totalTime = if (timerState != TimerState.EXPIRED) {
                        when (workoutState) {
                            WorkoutState.STARTING -> TIMER_STARTING_IN_TIME
                            WorkoutState.BREAK -> workout.pauseTime
                            WorkoutState.WORK -> workout.exerciseTime
                        }
                    } else TIMER_STARTING_IN_TIME
            )

            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .layoutId("buttonRow"),
                    horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                timerState.let {
                    when (it) {
                        TimerState.EXPIRED -> {
                            Button(
                                    onClick = {
                                        sendCommand(Constants.ACTION_START)
                                    },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = buttonModifier
                            ) {
                                Text("Start")
                            }
                        }
                        TimerState.RUNNING -> {
                            Button(
                                    onClick = { sendCommand(Constants.ACTION_PAUSE) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = buttonModifier
                            ) {
                                Text("Pause")
                            }

                            Button(
                                    onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = buttonModifier
                            ) {
                                Text("Cancel")
                            }
                        }
                        TimerState.PAUSED -> {
                            Button(
                                    onClick = { sendCommand(Constants.ACTION_RESUME) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = buttonModifier
                            ) {
                                Text("Resume")
                            }

                            Button(
                                    onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = buttonModifier
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun portraitConstraints(): ConstraintSet {
    return ConstraintSet {
        val buttonRow = createRefFor("buttonRow")
        val progCircle = createRefFor("progCircle")

        constrain(progCircle) {
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }

        constrain(buttonRow) {
            bottom.linkTo(parent.bottom, 64.dp)
        }
    }
}

private fun landscapeConstraints(): ConstraintSet {
    return ConstraintSet {
        val buttonRow = createRefFor("buttonRow")
        val progCircle = createRefFor("progCircle")

        constrain(buttonRow) {
            bottom.linkTo(parent.bottom, 32.dp)
        }

        constrain(progCircle) {
            top.linkTo(parent.top)
            centerHorizontallyTo(parent)
        }
    }
}
