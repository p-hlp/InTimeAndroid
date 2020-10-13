package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.utils.Constants.ONE_SECOND
import com.example.intimesimple.utils.getFormattedStopWatchTime

@Composable
fun TimeInputField(
        title: String,
        initValue: Long
) {
    var time by remember { mutableStateOf(initValue) }
    ConstraintLayout() {
        val (
                minusButton,
                plusButton,
                timerText,
                titleText
        ) = createRefs()

        Text(
                text = title.toUpperCase(),
                style = typography.subtitle1,
                modifier = Modifier.constrainAs(titleText){
                    bottom.linkTo(timerText.top, 4.dp)
                    centerHorizontallyTo(parent)
                }
        )

        Text(
                text = getFormattedStopWatchTime(time),
                style = typography.h3,
                modifier = Modifier.constrainAs(timerText){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
        )

        FloatingActionButton(
                onClick = {
                    if(time - ONE_SECOND >= 0L){
                        time -= ONE_SECOND
                    }
                },
                modifier = Modifier.constrainAs(minusButton){
                    absoluteRight.linkTo(timerText.absoluteLeft, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icon(Icons.Filled.RemoveCircleOutline)},
                elevation = 0.dp
        )

        FloatingActionButton(
                onClick = {
                    time += ONE_SECOND
                },
                modifier = Modifier.constrainAs(plusButton){
                    absoluteLeft.linkTo(timerText.absoluteRight, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icon(Icons.Filled.AddCircleOutline)},
                elevation = 0.dp
        )

    }
}

@Composable
fun RepsInputField(
        modifier: Modifier = Modifier,
        title: String,
        initValue: Int
) {
    var reps by remember { mutableStateOf(initValue) }
    ConstraintLayout(modifier) {
        val (
                minusButton,
                plusButton,
                timerText,
                titleText
        ) = createRefs()

        Text(
                text = title.toUpperCase(),
                style = typography.subtitle1,
                modifier = Modifier.constrainAs(titleText){
                    bottom.linkTo(timerText.top, 4.dp)
                    centerHorizontallyTo(parent)
                }
        )

        Text(
                text = reps.toString(),
                style = typography.h3,
                modifier = Modifier.constrainAs(timerText){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
        )

        FloatingActionButton(
                onClick = {
                    if(reps - 1 >= 0){
                        reps -= 1
                    }
                },
                modifier = Modifier.constrainAs(minusButton){
                    absoluteRight.linkTo(timerText.absoluteLeft, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icon(Icons.Filled.RemoveCircleOutline)},
                elevation = 0.dp
        )

        FloatingActionButton(
                onClick = {
                    reps += 1
                },
                modifier = Modifier.constrainAs(plusButton){
                    absoluteLeft.linkTo(timerText.absoluteRight, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icon(Icons.Filled.AddCircleOutline)},
                elevation = 0.dp
        )

    }
}



@Preview
@Composable
fun TimeInputFieldPreview(){
    MaterialTheme {
        TimeInputField( "Work", 30000L)
    }
}