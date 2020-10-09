package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.services.TestService
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants

@Composable
fun TestScreen(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit,
        workoutDetailViewModel: WorkoutDetailViewModel
) {
    val timerState = TestService.timerState.observeAsState(TimerState.EXPIRED)
    val timeInMillis = TestService.timeInMillis.observeAsState()
    var exTimeInMillis: Long? = null
    val workout = workoutDetailViewModel.workout.observeAsState().also {
        exTimeInMillis = it.value?.exerciseTime
    }

    ConstraintLayout(modifier = modifier) {
        val buttonRow = createRef()
        val timerText = createRef()
        val stateText = createRef()

        Text(
                text = "${
                    if (timerState.value == TimerState.EXPIRED)
                        exTimeInMillis ?: ""
                    else timeInMillis.value
                }",
                modifier = Modifier.constrainAs(timerText){
                    bottom.linkTo(stateText.top, 8.dp)
                    centerHorizontallyTo(parent)
                },
                color = Color.White,
                style = typography.h3
        )

        Text(
                text = timerState.value?.stateName!!,
                modifier = Modifier.constrainAs(stateText){
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                },
                color = Color.White,
                style = typography.h3
        )

        Row(
                modifier = Modifier.fillMaxWidth()
                        .constrainAs(buttonRow){
                            bottom.linkTo(parent.bottom, 32.dp)
                        },
                horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            timerState.value?.let {
                when (it) {
                    TimerState.EXPIRED -> {
                        Button(
                                onClick = { sendCommand(Constants.ACTION_START) },
                                shape = RoundedCornerShape(50)
                        ) {
                            Text("Start")
                        }
                    }
                    TimerState.RUNNING -> {
                        Button(
                                onClick = { sendCommand(Constants.ACTION_PAUSE) },
                                shape = RoundedCornerShape(50)
                        ) {
                            Text("Pause")
                        }

                        Button(
                                onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                shape = RoundedCornerShape(50)
                        ) {
                            Text("Cancel")
                        }
                    }
                    TimerState.PAUSED -> {
                        Button(
                                onClick = { sendCommand(Constants.ACTION_RESUME) },
                                shape = RoundedCornerShape(50)
                        ) {
                            Text("Resume")
                        }

                        Button(
                                onClick = { sendCommand(Constants.ACTION_CANCEL) },
                                shape = RoundedCornerShape(50)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}
