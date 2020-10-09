package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.utils.Constants

@Composable
fun TestScreen(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit
) {

    ConstraintLayout(modifier = modifier) {
        val buttonRow = createRef()

        Row(
                modifier = Modifier.fillMaxWidth()
                        .constrainAs(buttonRow){
                            bottom.linkTo(parent.bottom)
                        },
                horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                    onClick = { sendCommand(Constants.ACTION_START) },
                    shape = RoundedCornerShape(50)
            ) {
                Text("Start")
            }

            Button(
                    onClick = { sendCommand(Constants.ACTION_RESUME) },
                    shape = RoundedCornerShape(50)
            ) {
                Text("Resume")
            }

            Button(
                    onClick = { sendCommand(Constants.ACTION_PAUSE) },
                    shape = RoundedCornerShape(50)
            ) {
                Text("Pause")
            }

            Button(
                    onClick = { sendCommand(Constants.ACTION_CANCEL) },
                    shape = RoundedCornerShape(50)
            ) {
                Text("Cancel")
            }
        }
    }
}


@Preview
@Composable
fun TestScreenPreview() {
    MaterialTheme {
        TestScreen(
                Modifier.fillMaxSize()
        ) {}
    }
}