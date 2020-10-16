package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.AudioState
import com.example.intimesimple.services.TimerService
import com.example.intimesimple.utils.audioStateToIcon
import com.example.intimesimple.utils.getNextAudioStateAction

@Composable
fun DetailScreenTopBar(
        modifier: Modifier = Modifier,
        title: String,
        navigateHome: () -> Unit,
        sendCommand: (String) -> Unit
){
    TopAppBar(
            title = {
                Text(title)
            },
            navigationIcon = {
                IconButton(onClick = {navigateHome()}) {
                    Icon(Icons.Filled.ArrowBack)
                }
            },
            actions = {
                TopBarActions(
                        sendCommand = sendCommand
                )
            }
    )
}


@Composable
fun TopBarActions(
        sendCommand: (String) -> Unit
){
    val audioState by TimerService.audioState.observeAsState(AudioState.MUTE)
    IconButton(
            onClick = {
                sendCommand(getNextAudioStateAction(audioState))
            }
    ) {
        Icon(asset = audioStateToIcon(audioState))
    }
}

@Preview
@Composable
fun DetailScreenTopBarPreview(){
    DetailScreenTopBar(title = "Test",
    navigateHome = {},
    sendCommand = {})
}