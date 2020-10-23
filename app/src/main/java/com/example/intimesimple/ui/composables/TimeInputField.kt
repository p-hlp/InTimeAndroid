package com.example.intimesimple.ui.composables

import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.AmbientTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.CoreTextField
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.ViewAmbient
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.utils.getFormattedStopWatchTime

@Composable
fun TimeInputField(
        modifier: Modifier = Modifier,
        title: String,
        text: String,
        onPlusPressed: () -> Unit,
        onMinusPressed: () -> Unit
) {
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
                text = text,
                style = typography.h3,
                modifier = Modifier.constrainAs(timerText){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
        )

        FloatingActionButton(
                onClick = {onMinusPressed()},
                modifier = Modifier.constrainAs(minusButton){
                    absoluteRight.linkTo(timerText.absoluteLeft, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icons.Filled.Remove}
        )

        FloatingActionButton(
                onClick = { onPlusPressed() },
                modifier = Modifier.constrainAs(plusButton){
                    absoluteLeft.linkTo(timerText.absoluteRight, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {Icons.Filled.Add},
        )

    }
}

@Composable
fun RepsInputField(
        modifier: Modifier = Modifier,
        title: String,
        text: String,
        onMinusPressed: () -> Unit,
        onPlusPressed: () -> Unit
) {
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
                text = text,
                style = typography.h3,
                modifier = Modifier.constrainAs(timerText){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
        )

        FloatingActionButton(
                onClick = {
                    onMinusPressed()
                },
                modifier = Modifier.constrainAs(minusButton){
                    absoluteRight.linkTo(timerText.absoluteLeft, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {
                    Icon(Icons.Filled.Remove)
                },
        )

        FloatingActionButton(
                onClick = {
                    onPlusPressed()
                },
                modifier = Modifier.constrainAs(plusButton){
                    absoluteLeft.linkTo(timerText.absoluteRight, 32.dp)
                    centerVerticallyTo(parent)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color.Transparent,
                icon = {
                    Icon(Icons.Filled.Add)
                }
        )
    }
}



@Preview
@Composable
fun TimeInputFieldPreview(){
    MaterialTheme {
        newTimeInput()
    }
}


@Composable
fun newTimeInput(modifier: Modifier = Modifier){
    // TODO: 3 InputFields for Hours/Minutes/Seconds
    var hoursText by savedInstanceState { "00" }
//    var minutesText by savedInstanceState { "" }
//    var secondsText by savedInstanceState { "" }
    val isValidHours = hoursText.count() in 1..2
//    val isValidMinutes = hoursText.count() in 1..2
//    val isValidSeconds = hoursText.count() in 1..2
    val sizeInDp = with(DensityAmbient.current){ typography.body1.fontSize.toDp()}

    TextField(
            value = hoursText,
            onValueChange = {
                when (it.count()){
                    in 0..2 -> hoursText = it
                }
            },
            modifier = Modifier.width(sizeInDp.times(4)),
            placeholder = {Text("00")},
            isErrorValue = !isValidHours,
            keyboardType = KeyboardType.Number,
            textStyle = typography.body1
    )
}