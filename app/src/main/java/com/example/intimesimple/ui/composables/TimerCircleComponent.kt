package com.example.intimesimple.ui.composables


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.intimesimple.ui.theme.Green500
import com.example.intimesimple.utils.calculateRadiusOffset
import kotlin.math.min

@Composable
fun TimerCircleComponent(
        modifier: Modifier = Modifier,
        screenWidthDp: Int,
        screenHeightDp: Int,
        time: String,
        state: String,
        reps: String,
        elapsedTime: Long,
        totalTime: Long
){
        val maxRadius by remember { mutableStateOf(min(screenHeightDp, screenWidthDp)) }

        Box(
                modifier = modifier.size(maxRadius.dp).padding(8.dp)
        ){

                //TODO: change layout depending on orientation
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (timerText, workoutStateText, repText) = createRefs()

                        Text(
                                modifier = Modifier.constrainAs(timerText){
                                        centerHorizontallyTo(parent)
                                        centerVerticallyTo(parent)
                                },
                                text = time,
                                style = typography.h2,
                        )

                        Text(
                                modifier = Modifier.constrainAs(workoutStateText){
                                        centerHorizontallyTo(parent)
                                        bottom.linkTo(timerText.top, 8.dp)
                                },
                                text = state,
                                style = typography.h5,
                        )

                        Text(
                                modifier = Modifier.constrainAs(repText){
                                        centerHorizontallyTo(parent)
                                        top.linkTo(timerText.bottom, 8.dp)
                                },
                                text = reps,
                                style = typography.h5,
                        )
                }

                // only show in portrait mode
                if(screenWidthDp.dp < 600.dp){
                        TimerCircle(
                                modifier = modifier,
                                elapsedTime = elapsedTime,
                                totalTime = totalTime
                        )
                }
        }
}

@Composable
fun TimerCircle(
        modifier: Modifier = Modifier,
        elapsedTime: Long,
        totalTime: Long
){
        Box(modifier.fillMaxSize().drawWithCache {
                val height = size.height
                val width = size.width
                val dotDiameter = 12.dp
                val strokeSize = 20.dp
                val radiusOffset
                        = calculateRadiusOffset(strokeSize.value, dotDiameter.value, 0f)

                val xCenter = width/2f
                val yCenter = height/2f
                val radius = min(xCenter, yCenter)
                val arcWidthHeight = ((radius - radiusOffset) * 2f)
                val arcSize = Size(arcWidthHeight, arcWidthHeight)

                val remainderColor = Color.White.copy(alpha = 0.25f)
                val completedColor = Green500

                val whitePercent =
                        min(1f, elapsedTime.toFloat()/totalTime.toFloat())
                val greenPercent = 1 - whitePercent

                onDraw {
                        drawArc(
                                completedColor,
                                270f,
                                -greenPercent * 360f,
                                false,
                                topLeft = Offset(radiusOffset, radiusOffset),
                                size = arcSize,
                                style = Stroke(width = strokeSize.value)
                        )

                        drawArc(
                                remainderColor,
                                270f,
                                whitePercent*360,
                                false,
                                topLeft = Offset(radiusOffset, radiusOffset),
                                size = arcSize,
                                style = Stroke(width = strokeSize.value)
                        )
                }
        })
}

@Composable
fun DebugCenterLines(
        modifier: Modifier
){
        Canvas(modifier = modifier.fillMaxSize(), onDraw = {
                drawLine(
                        color = Color.Black,
                        start = Offset(size.width/2f, 0f),
                        end = Offset(size.width/2f, size.height),
                        strokeWidth = 4f
                )

                drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height/2f),
                        end = Offset(size.width, size.height/2f),
                        strokeWidth = 4f
                )
        })
}