package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.intimesimple.ui.theme.INTimeDialogThemeOverlay


@Composable
fun INTimeAlertDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    bodyText: String,
    buttonAcceptText: String,
    buttonDismissText: String
) {
    INTimeDialogThemeOverlay {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = { Text(bodyText) },
            buttons = {
                Column {
                    Divider(
                        Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = onAccept,
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(buttonAcceptText)
                        }
                        TextButton(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(buttonDismissText)
                        }
                    }
                }
            }
        )
    }
}


@Preview
@Composable
fun INTimeAlertDialogPreview(){
    INTimeAlertDialog(
        onAccept = {},
        onDismiss = {},
        bodyText = "THIS IS A TEST",
        buttonAcceptText = "Add",
        buttonDismissText = "Cancel"
    )
}