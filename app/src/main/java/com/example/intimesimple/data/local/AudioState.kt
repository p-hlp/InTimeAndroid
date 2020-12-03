package com.example.intimesimple.data.local

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.graphics.vector.ImageVector


enum class AudioState{
    MUTE,
    VIBRATE,
    SOUND
}

enum class VolumeButtonState(val asset: ImageVector){
    MUTE(Icons.Filled.VolumeOff),
    VIBRATE(Icons.Filled.Vibration),
    SOUND(Icons.Filled.VolumeUp)
}