package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun TimerInputField(
        modifier: Modifier = Modifier,
        hourSlot: @Composable () -> Unit,
        minuteSlot: @Composable () -> Unit,
        secondSlot: @Composable () -> Unit
){
    Surface(modifier.fillMaxWidth().height(100.dp).padding(16.dp), color = Color.LightGray) {
        val configuration = ConfigurationAmbient.current
        val screenWidth = configuration.screenWidthDp
        val third = (screenWidth / 3).dp

        Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface(
                    modifier = Modifier.width(third).fillMaxHeight(),
                    color = Color.Green
            ) {
                hourSlot()
            }

            Surface(
                    modifier = Modifier.width(third).fillMaxHeight(),
                    color = Color.Red
            ) {
                minuteSlot()
            }

            Surface(
                    modifier = Modifier.width(third).fillMaxHeight(),
                    color = Color.Yellow
            ) {
                secondSlot()
            }
        }
    }
}

@Preview
@Composable
fun TimerInputFieldPreview(){
    TimerInputField(
            hourSlot = {},
            minuteSlot = {},
            secondSlot = {}
    )
}