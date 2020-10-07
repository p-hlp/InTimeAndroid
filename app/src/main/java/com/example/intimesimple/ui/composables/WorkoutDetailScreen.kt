package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.data.local.defaultWorkouts
import com.example.intimesimple.ui.theme.INTimeTheme
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.Constants.ACTION_PAUSE
import com.example.intimesimple.utils.Constants.ACTION_RESUME
import com.example.intimesimple.utils.Constants.ACTION_START
import com.example.intimesimple.utils.getFormattedStopWatchTime

@Composable
fun WorkoutDetailScreen(
        modifier: Modifier = Modifier,
        wId: Long,
        navigateHome: () -> Unit,
        onServiceCommand: (String) -> Unit
) {
    // Get specific workout via viewmodel or whatever
    val workout = defaultWorkouts[wId.toInt()]

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
    val timerState = remember { mutableStateOf(TimerState.EXPIRED) }
    ConstraintLayout(modifier) {

        val (buttonSurface, timerText) = createRefs()

        Text(
                getFormattedStopWatchTime(workout.exerciseTime),
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

                when (timerState.value) {
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

@Preview
@Composable
fun WorkoutDetailBodyContentPreview() {
    val workout = defaultWorkouts[0]
    INTimeTheme {
        WorkoutDetailBodyContent(
                modifier = Modifier.fillMaxSize(),
                workout = workout,
                onServiceCommand = {}
        )
    }
}