package com.example.intimesimple.ui.composables

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.data.local.TimerState
import com.example.intimesimple.utils.calculateRadiusOffset
import kotlin.math.min

@Composable
fun TimerCircle(
        modifier: Modifier = Modifier,
        timerState: TimerState,
        elapsedTime: Long,
        totalTime: Long
){
    Canvas(
            modifier = modifier, onDraw = {

        val dotDiameter = 12.dp
        val dotRadius = dotDiameter / 2f
        val strokeSize = 4.dp
        val radiusOffset
                = calculateRadiusOffset(strokeSize.value, dotDiameter.value, 0f)

        val xCenter = this.size.width/2f
        val yCenter = this.size.height/2f
        val radius = min(xCenter, yCenter) - radiusOffset

        val remainderColor = Color.White
        val completedColor = Color.Blue

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

        when (timerState){
            TimerState.EXPIRED -> {

            }
            else -> {
                arcRect.top = yCenter - radius
                arcRect.bottom = yCenter + radius
                arcRect.left = xCenter - radius
                arcRect.right = xCenter + radius

                val redPercent =
                        min(1f, elapsedTime.toFloat()/totalTime.toFloat())
                val whitePercent = 1 - redPercent

                drawArc(
                        remainderColor,
                        270f,
                        whitePercent * 360f,
                        false,
                        size = Size(radius*2f, radius*2f),
                        style = arcStroke
                )

                drawArc(
                        completedColor,
                        270f,
                        -redPercent*360,
                        false,
                        size = Size(radius*2f, radius*2f),
                        style = arcStroke
                )
            }
        }


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