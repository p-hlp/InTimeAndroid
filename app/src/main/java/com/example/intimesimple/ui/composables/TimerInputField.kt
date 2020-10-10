package com.example.intimesimple.ui.composables

import android.util.Range
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun TimerInputField(
        modifier: Modifier = Modifier,
        hourSlot: @Composable () -> Unit
){
    Surface(modifier.fillMaxWidth().height(100.dp).padding(16.dp), color = Color.LightGray) {
        Surface(
                modifier = Modifier.fillMaxSize()
        ) {
            hourSlot()
        }
    }
}


@Composable
fun timerInput(items: List<Int>){
    LazyColumnForIndexed(
            items = items,
            contentPadding = PaddingValues(top = 4.dp)
    ) { index, item ->
        timerInputNumber(
                modifier = Modifier.fillMaxSize(),
                number = item
        )
    }
}

@Composable
fun timerInputNumber(modifier: Modifier = Modifier, number: Int){
    Box(modifier){
        Text(
                text = "$number",
                style = typography.h5,
                modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview
@Composable
fun TimerInputFieldPreview(){
    val hours = (0..99).toList()
    val minutesAndSeconds = (0..60).toList()

    TimerInputField(
            hourSlot = { timerInput(items = hours) }
    )
}