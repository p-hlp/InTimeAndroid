package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun TestScreen(
        modifier: Modifier = Modifier,
        sendCommand: (String) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val startButton = createRef()
        val timerText = createRef()

        Text(
                text = "Test",
                style = typography.h3,
                modifier = Modifier.constrainAs(timerText){
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                }
        )

        Button(
                onClick = {
                    sendCommand("TEST_ACTION")
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.constrainAs(startButton) {
                    bottom.linkTo(parent.bottom, 32.dp)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text("Start")
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