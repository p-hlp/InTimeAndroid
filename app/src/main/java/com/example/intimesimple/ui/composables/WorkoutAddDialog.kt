package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.viewmodels.WorkoutListViewModel
import com.example.intimesimple.utils.Constants.ONE_SECOND
import com.example.intimesimple.utils.getFormattedStopWatchTime
import com.example.intimesimple.utils.getRandomWorkout
import timber.log.Timber

@Composable
fun WorkoutAddAlertDialog(
    workoutListViewModel: WorkoutListViewModel,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    INTimeDialogThemeOverlay {
        var nameText by remember { mutableStateOf(TextFieldValue("")) }
        var workTime by remember { mutableStateOf(40000L) }
        var pauseTime by remember { mutableStateOf(15000L) }
        var setCount by remember { mutableStateOf(1) }
        var textFieldError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Column {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = {s ->
                            if(s.text.count { it == '\n' } < 1){
                                nameText = s
                            }
                        },
                        label = {Text("Enter a workout name here")},
                        isErrorValue = textFieldError,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp).fillMaxWidth()
                    )

                    TimeInputField(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        "Work",
                        getFormattedStopWatchTime(workTime),
                        onPlusPressed = {
                            workTime += ONE_SECOND
                        },
                        onMinusPressed = {
                            if(workTime - ONE_SECOND >= 0L){
                                workTime -= ONE_SECOND
                            }
                        }
                    )
                    TimeInputField(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        "Pause",
                        getFormattedStopWatchTime(pauseTime),
                        onPlusPressed = {
                            pauseTime += ONE_SECOND
                        },
                        onMinusPressed = {
                            if(pauseTime - ONE_SECOND >= 0L){
                                pauseTime -= ONE_SECOND
                            }
                        }
                    )
                    RepsInputField(
                        title = "Sets",
                        text = setCount.toString(),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onPlusPressed = {
                            setCount += 1
                        },
                        onMinusPressed = {
                            if(setCount - 1 >= 0){
                                setCount -= 1
                            }
                        }
                    )
                }
            },
            buttons = {
                Column {
                    Divider(
                        Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                // TODO: Get values from fields, build and insert
                                if(nameText.text.isNotEmpty()){
                                    val workoutNew = Workout(
                                        name = nameText.text,
                                        exerciseTime = workTime,
                                        pauseTime = pauseTime,
                                        repetitions = setCount
                                    )
                                    workoutListViewModel.addWorkout(workoutNew)
                                    textFieldError = false
                                    onAccept()
                                }else{
                                    textFieldError = true
                                }
                            },
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text("ADD")
                        }
                        TextButton(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text("CANCEL")
                        }
                    }
                }
            }
        )
    }
}



@Composable
fun INTimeDialogThemeOverlay(content: @Composable () -> Unit) {
    val dialogColors = darkColors(
        primary = Color.White,
        surface = Color.White.copy(alpha = 0.12f).compositeOver(Color.Black),
        onSurface = Color.White
    )

    val currentTypography = MaterialTheme.typography
    val dialogTypography = currentTypography.copy(
        subtitle1 = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        body2 = currentTypography.body1.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 1.sp
        ),
        button = currentTypography.button.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.2.em
        )
    )
    MaterialTheme(colors = dialogColors, typography = dialogTypography, content = content)
}