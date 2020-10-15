package com.example.intimesimple.ui.composables

import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.VolumeButtonState
import com.example.intimesimple.utils.Constants.ACTION_MUTE
import com.example.intimesimple.utils.Constants.ACTION_SOUND
import com.example.intimesimple.utils.Constants.ACTION_VIBRATE
import timber.log.Timber

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
    var buttonState by androidx.compose.runtime.remember{ androidx.compose.runtime.mutableStateOf(VolumeButtonState.MUTE) }

    IconButton(
            onClick = {
                buttonState = when(buttonState){
                    VolumeButtonState.MUTE -> VolumeButtonState.VIBRATE
                    VolumeButtonState.VIBRATE -> VolumeButtonState.VOLUME
                    VolumeButtonState.VOLUME -> VolumeButtonState.MUTE
                }

                sendCommand(
                        when(buttonState){
                            VolumeButtonState.MUTE -> ACTION_MUTE
                            VolumeButtonState.VIBRATE -> ACTION_VIBRATE
                            VolumeButtonState.VOLUME -> ACTION_SOUND
                        }
                )
                Timber.d("IconButton clicked")
            }
    ) {
        Icon(asset = buttonState.asset)
    }
}

@Preview
@Composable
fun DetailScreenTopBarPreview(){
    DetailScreenTopBar(title = "Test",
    navigateHome = {},
    sendCommand = {})
}