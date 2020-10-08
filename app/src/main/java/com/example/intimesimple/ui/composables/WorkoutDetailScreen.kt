package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START


@Composable
fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    navigateHome: () -> Unit,
    onServiceCommand: (String) -> Unit,
    workoutDetailViewModel: WorkoutDetailViewModel
) {
    // Get specific workout via viewmodel or whatever
    val workout = Workout(0, "15min Posture", 35000L, 15000L, 18)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = workout.name.toUpperCase()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onServiceCommand(ACTION_CANCEL)
                        navigateHome()
                    }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                }
            )
        },
        bodyContent = {
            WorkoutDetailBodyContent(
                Modifier.fillMaxSize(),
                workout,
                onServiceCommand
            )
        }
    )

}

@Composable
fun WorkoutDetailBodyContent(
    modifier: Modifier = Modifier,
    workout: Workout,
    onServiceCommand: (String) -> Unit
) {
    val configuration = ConfigurationAmbient.current
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = 0.3f * screenWidth
    val timerState = TimerService.timerState.observeAsState(TimerState.EXPIRED)
    val timerMillis = TimerService.timeLeftInMillis.observeAsState(workout.exerciseTime)
    val timeLeftInSeconds =
        TimerService.timeLeftInSeconds.observeAsState((workout.exerciseTime / 1000L).toInt())

    ConstraintLayout(modifier) {

        val (buttonSurface, timerText) = createRefs()

        Text(
            (timeLeftInSeconds.value!! * 1000L).toString(),
            Modifier.constrainAs(timerText) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            },
            style = typography.h4
        )

        Box(
            modifier = Modifier.constrainAs(buttonSurface) {
                bottom.linkTo(parent.bottom, 32.dp)
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val buttonModifier = Modifier
                    .width(buttonWidth.dp)
                    .padding(8.dp)

                timerState.value?.let {
                    when (it) {
                        TimerState.EXPIRED -> {
                            Button(
                                onClick = { onServiceCommand(ACTION_START) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier,
                            ) {
                                Text("Start")
                            }
                        }
                        TimerState.RUNNING -> {
                            Button(
                                onClick = { onServiceCommand(ACTION_PAUSE) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Pause")
                            }

                            Button(
                                onClick = { onServiceCommand(ACTION_CANCEL) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Cancel")
                            }
                        }
                        TimerState.PAUSED -> {
                            Button(
                                onClick = { onServiceCommand(ACTION_RESUME) },
                                shape = RoundedCornerShape(50),
                                modifier = buttonModifier
                            ) {
                                Text("Resume")
                            }

                            Button(
                                onClick = { onServiceCommand(ACTION_CANCEL) },
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

@Preview
@Composable
fun WorkoutDetailBodyContentPreview() {
    val workout = Workout(0, "15min Posture", 35000L, 15000L, 18)
    WorkoutDetailBodyContent(
        modifier = Modifier.fillMaxSize(),
        workout = workout,
        onServiceCommand = {}
    )
}