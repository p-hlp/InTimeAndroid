package com.example.intimesimple.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.CoreTextField
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun TimeInputField(
        modifier: Modifier = Modifier,
        hoursField: @Composable () -> Unit,
        minutesField: @Composable () -> Unit,
        secondsField: @Composable () -> Unit
){
    Surface(
            modifier
    ) {
        Row(
                horizontalArrangement = Arrangement.SpaceEvenly
        ){
            hoursField()
            minutesField()
            secondsField()
        }
    }
}

@Composable
fun DigitInputField(
        modifier: Modifier = Modifier,
        initValue: String
){
    Surface(
            modifier = modifier,
            color = Color.LightGray,
            shape = RoundedCornerShape(8.dp)
    ){
        var textValue by remember { mutableStateOf(TextFieldValue(initValue)) }
        TextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                },
                textStyle = typography.body2,
                keyboardType = KeyboardType.NumberPassword
        )
    }
}


@Preview
@Composable
fun DigitInputFieldPreview(){
    DigitInputField(initValue = "00")
}

@Preview
@Composable
fun TimeInputFieldPreview() {
    TimeInputField(
            hoursField = { DigitInputField(initValue = "00") },
            minutesField = { DigitInputField(initValue = "00") },
            secondsField = { DigitInputField(initValue = "45") },
            modifier = Modifier.fillMaxWidth()
    )
}
