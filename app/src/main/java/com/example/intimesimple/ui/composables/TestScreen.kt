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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.services.TestService
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants
import com.example.intimesimple.utils.getFormattedStopWatchTime
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.LayoutDirectionAmbient
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import com.example.intimesimple.data.local.Workout

@Composable
fun TestScreen(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit,
        navigateHome: () -> Unit,
        workoutDetailViewModel: WorkoutDetailViewModel
) {
    val workout by workoutDetailViewModel.workout.observeAsState()

   Scaffold(
           modifier.fillMaxSize(),
           topBar = {
               TopAppBar(
                       title = {
                           workout?.name?.toUpperCase()?.let {
                               Text(
                                       text = it
                               )
                           }
                       },
                       navigationIcon = {
                           IconButton(onClick = {
                               navigateHome()
                           }) {
                               Icon(Icons.Filled.ArrowBack)
                           }
                       }
               )
           },
           bodyContent = {
               workout?.let { it1 ->
                   TestScreenContent(
                           modifier = modifier,
                           sendCommand,
                           it1
                   )
               }
           }
   )
}

@Composable
fun TestScreenContent(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit,
        workout: Workout
){
    val timerState by TestService.timerState.observeAsState(TimerState.EXPIRED)
    val timeInMillis by TestService.timeInMillis.observeAsState()
    val timerRepCount by TestService.repetitionCount.observeAsState()
    val exTimeInMillis: Long = workout.exerciseTime
    val repCount: Int = workout.repetitions
    val progressTime by TestService.progressTimeInMillis.observeAsState(exTimeInMillis)
    val configuration = ConfigurationAmbient.current
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = 0.3f * screenWidth

    WithConstraints(modifier) {

        val constraints = if (screenWidth.dp < 600.dp){
            portraitConstraints()
        }
        else landscapeConstraints()

        ConstraintLayout(modifier = modifier, constraintSet = constraints) {


            val buttonModifier = Modifier.width(buttonWidth.dp)
            Text(
                    text = (if (timerState == TimerState.EXPIRED)
                        getFormattedStopWatchTime(exTimeInMillis)
                    else getFormattedStopWatchTime(timeInMillis)),
                    color = Color.White,
                    style = typography.h2,
                    modifier = Modifier.layoutId("timerText")
            )

            // Only show in portrait
            if(screenWidth.dp < 600.dp){
                TimerCircle(
                        elapsedTime = if(timerState == TimerState.EXPIRED) exTimeInMillis else progressTime,
                        totalTime = exTimeInMillis,
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
                    text = "${
                        if (timerState == TimerState.EXPIRED)
                            repCount ?: ""
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
                                    onClick = { sendCommand(Constants.ACTION_START) },
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

private fun portraitConstraints(): ConstraintSet{
    return ConstraintSet {
        val buttonRow = createRefFor("buttonRow")
        val timerText = createRefFor("timerText")
        val stateText = createRefFor("stateText")
        val repText = createRefFor("repText")
        val progCircle = createRefFor("progCircle")

        constrain(progCircle){
            top.linkTo(parent.top, 8.dp)
        }

        constrain(buttonRow){
            bottom.linkTo(parent.bottom, 64.dp)
        }

        constrain(timerText){
            top.linkTo(parent.top, 172.dp)
            centerHorizontallyTo(parent)
        }

        constrain(stateText){
            top.linkTo(timerText.bottom, 16.dp)
            centerHorizontallyTo(parent)
        }

        constrain(repText){
            top.linkTo(stateText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }
    }
}

private fun landscapeConstraints(): ConstraintSet{
    return ConstraintSet {
        val buttonRow = createRefFor("buttonRow")
        val timerText = createRefFor("timerText")
        val stateText = createRefFor("stateText")
        val repText = createRefFor("repText")

        constrain(buttonRow){
            bottom.linkTo(parent.bottom, 32.dp)
        }

        constrain(timerText){
            top.linkTo(parent.top, 32.dp)
            centerHorizontallyTo(parent)
        }

        constrain(stateText){
            top.linkTo(timerText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }

        constrain(repText){
            top.linkTo(stateText.bottom, 8.dp)
            centerHorizontallyTo(parent)
        }
    }
}
