package com.example.intimesimple.ui.composables

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.utils.calculateRadiusOffset
import timber.log.Timber
import kotlin.math.min

@Composable
fun TimerCircle(
        modifier: Modifier = Modifier,
        timerState: TimerState,
        elapsedTime: Long,
        totalTime: Long
){
    Canvas(
            modifier = modifier.padding(8.dp).fillMaxSize(), onDraw = {

        Timber.d("TimerCircle called")

        val dotDiameter = 12.dp
        val dotRadius = dotDiameter / 2f
        val strokeSize = 12.dp
        val radiusOffset
                = calculateRadiusOffset(strokeSize.value, dotDiameter.value, 0f)

        val xCenter = this.size.width/2f
        val yCenter = this.size.height/2f
        val radius = min(xCenter, yCenter)
        val arcWidthHeight = ((radius - radiusOffset) * 2f)
        val arcSize = Size(arcWidthHeight, arcWidthHeight)

        val remainderColor = Color.White
        val completedColor = Color.Red

        val arcRect = RectF()
        val circlePaint = Paint()
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.STROKE

        val arcStroke = Stroke(
                width = strokeSize.value
        )
        val circleFill = Paint()
        circleFill.isAntiAlias = true
        circleFill.style = Paint.Style.FILL

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
            timerState = TimerState.RUNNING,
            elapsedTime = 5000L,
            totalTime = 30000L
    )
}