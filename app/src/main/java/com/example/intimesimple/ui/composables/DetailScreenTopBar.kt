package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.intimesimple.ui.viewmodels.WorkoutDetailViewModel
import com.example.intimesimple.utils.Constants.ACTION_CANCEL
import com.example.intimesimple.utils.getNextVolumeButtonState
import com.example.intimesimple.utils.getTimerActionFromVolumeButtonState

@Composable
fun DetailScreenTopBar(
        modifier: Modifier = Modifier,
        title: String,
        navController: NavController,
        sendCommand: (String) -> Unit,
        workoutDetailViewModel: WorkoutDetailViewModel
){
    val buttonState by workoutDetailViewModel
        .volumeButtonState
        .observeAsState()

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(
                onClick = {
                    // navigate back
                    navController.popBackStack()
                    // send command cancel
                    sendCommand(ACTION_CANCEL)
                },
                icon = {
                    Icon(Icons.Filled.ArrowBack)
                }
            )
        },
        actions = {
            IconButton(
                onClick = {
                    buttonState?.let {
                        val nextState = getNextVolumeButtonState(it)
                        workoutDetailViewModel.setSoundState(nextState.name)
                        sendCommand(getTimerActionFromVolumeButtonState(nextState))
                    }

                },
                icon = {
                    buttonState?.let {
                        Icon(it.asset)
                    }
                }
            )
        }
    )
}


