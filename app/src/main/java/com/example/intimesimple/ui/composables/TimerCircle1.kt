package com.example.intimesimple.ui.composables

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.utils.calculateRadiusOffset
import timber.log.Timber
import kotlin.math.min

@Composable
fun TimerCircle(
        modifier: Modifier = Modifier,
        elapsedTime: Long,
        totalTime: Long
){
    //Timber.d("Circle: elapsedTime: $elapsedTime - totalTime: $totalTime")
    Canvas(
            modifier = modifier.padding(16.dp).fillMaxSize(), onDraw = {

        val dotDiameter = 12.dp
        val strokeSize = 16.dp
        val radiusOffset
                = calculateRadiusOffset(strokeSize.value, dotDiameter.value, 0f)

        val xCenter = this.size.width/2f
        val yCenter = this.size.height/2f
        val radius = min(xCenter, yCenter)
        val arcWidthHeight = ((radius - radiusOffset) * 2f)
        val arcSize = Size(arcWidthHeight, arcWidthHeight)

        val remainderColor = Color.White.copy(alpha = 0.25f)
        val completedColor = Green500


        val arcStroke = Stroke(
                width = strokeSize.value
        )

        val redPercent =
                min(1f, elapsedTime.toFloat()/totalTime.toFloat())
        val whitePercent = 1 - redPercent

        drawArc(
                completedColor,
                270f,
                -whitePercent * 360f,
                false,
                topLeft = Offset(radiusOffset, radiusOffset),
                size = arcSize,
                style = arcStroke
        )

        drawArc(
                remainderColor,
                270f,
                redPercent*360,
                false,
                topLeft = Offset(radiusOffset, radiusOffset),
                size = arcSize,
                style = arcStroke
        )

    })
}

@Preview
@Composable
fun TimerCirclePreview(){
    TimerCircle(
            modifier = Modifier.fillMaxSize(),
            elapsedTime = 5000L,
            totalTime = 30000L
    )
}