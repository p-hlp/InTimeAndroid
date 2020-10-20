package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.getFormattedStopWatchTime
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.navigation.NavController
import com.example.intimesimple.MainActivity
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.WorkoutState
import com.example.intimesimple.utils.Constants.TIMER_STARTING_IN_TIME


@Composable
fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    workoutDetailViewModel: WorkoutDetailViewModel,
    sendServiceCommand: (String) -> Unit
) {
    val workout by workoutDetailViewModel.workout.observeAsState()

    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            workout?.let {
                DetailScreenTopBar(
                    title = it.name,
                    navigateHome = {
                        if(navController.previousBackStackEntry != null){
                            navController.popBackStack()
                        }
                    },
                    sendCommand = sendServiceCommand,
                    workoutDetailViewModel = workoutDetailViewModel
                )
            }

        },
        bodyContent = {
            workout?.let { it1 ->
                WorkoutDetailScreenContent(
                    modifier = modifier,
                    sendServiceCommand,
                    it1,
                    workoutDetailViewModel
                )
            }
        }
    )
}


// TODO: Redesign input for workout -> exploding fab to new screen
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
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = 0.3f * screenWidth

    WithConstraints(modifier) {

        val constraints = if (screenWidth.dp < 600.dp) {
            portraitConstraints()
        } else landscapeConstraints()

        ConstraintLayout(modifier = modifier, constraintSet = constraints) {


            val buttonModifier = Modifier.width(buttonWidth.dp)
            Text(
                text = (if (timerState == TimerState.EXPIRED)
                    getFormattedStopWatchTime(TIMER_STARTING_IN_TIME)
                else getFormattedStopWatchTime(timeInMillis)),
                color = Color.White,
                style = typography.h2,
                modifier = Modifier.layoutId("timerText")
            )

            // Only show in portrait
            if (screenWidth.dp < 600.dp) {
                // TODO: Make this not ugly
                //Timber.d("WorkoutState: ${workoutState.stateName}")
                TimerCircle(
                    elapsedTime =
                    if (timerState == TimerState.EXPIRED) TIMER_STARTING_IN_TIME
                    else progressTime,
                    totalTime =
                    if (timerState != TimerState.EXPIRED) {
                        when (workoutState) {
                            WorkoutState.STARTING -> TIMER_STARTING_IN_TIME
                            WorkoutState.BREAK -> workout.pauseTime
                            WorkoutState.WORK -> workout.exerciseTime
                        }
                    } else TIMER_STARTING_IN_TIME,
                    modifier = Modifier.layoutId("progCircle")
                )
            }

            Text(
                text = timerState.stateName,
                color = Color.White,
                style = typography.h5,
                modifier = Modifier.layoutId("stateText")
            )

            Text(
                text = workoutState.name,
                color = Color.White,
                style = typography.h5,
                modifier = Modifier.layoutId("workoutStateText")
            )

            Text(
                text = "${
                    if (timerState == TimerState.EXPIRED)
                        repCount
                    else timerRepCount
                }",
                color = Color.White,
                style = typography.h5,
                modifier = Modifier.layoutId("repText")
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
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Start")
                            }
                        }
                        TimerState.RUNNING -> {
                            Button(
                                onClick = { sendCommand(Constants.ACTION_PAUSE) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Pause")
                            }

                            Button(
                                onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Cancel")
                            }
                        }
                        TimerState.PAUSED -> {
                            Button(
                                onClick = { sendCommand(Constants.ACTION_RESUME) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Resume")
                            }

                            Button(
                                onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                shape = RoundedCornerShape(50),
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
        val timerText = createRefFor("timerText")
        val stateText = createRefFor("stateText")
        val workoutStateText = createRefFor("workoutStateText")
        val repText = createRefFor("repText")
        val progCircle = createRefFor("progCircle")

        constrain(progCircle) {
            top.linkTo(parent.top, 8.dp)
        }

        constrain(buttonRow) {
            bottom.linkTo(parent.bottom, 64.dp)
        }

        constrain(timerText) {
            top.linkTo(parent.top, 172.dp)
            centerHorizontallyTo(parent)
        }

        constrain(workoutStateText) {
            bottom.linkTo(timerText.top, 16.dp)
            centerHorizontallyTo(parent)
        }

        constrain(stateText) {
            top.linkTo(timerText.bottom, 16.dp)
            centerHorizontallyTo(parent)
        }

        constrain(repText) {
            top.linkTo(stateText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }
    }
}

private fun landscapeConstraints(): ConstraintSet {
    return ConstraintSet {
        val buttonRow = createRefFor("buttonRow")
        val timerText = createRefFor("timerText")
        val stateText = createRefFor("stateText")
        val workoutStateText = createRefFor("workoutStateText")
        val repText = createRefFor("repText")

        constrain(buttonRow) {
            bottom.linkTo(parent.bottom, 32.dp)
        }

        constrain(timerText) {
            top.linkTo(parent.top, 32.dp)
            centerHorizontallyTo(parent)
        }

        constrain(workoutStateText) {
            top.linkTo(timerText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }
        constrain(stateText) {
            top.linkTo(workoutStateText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }

        constrain(repText) {
            top.linkTo(stateText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }
    }
}
