package com.example.intimesimple.ui.composables

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutAddAlertDialog(
        onAccept: () -> Unit,
        onDismiss: () -> Unit,
        bodyContent: @Composable () -> Unit,
        buttonAcceptText: String,
        buttonDismissText: String
) {
    INTimeDialogThemeOverlay {
        AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    bodyContent()
                },
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

@Composable
fun INTimeDialogThemeOverlay(content: @Composable () -> Unit) {
    val dialogColors = darkColors(
            primary = Color.White,
            surface = Color.White.copy(alpha = 0.12f).compositeOver(Color.Black),
            onSurface = Color.White
    )

    val currentTypography = MaterialTheme.typography
    val dialogTypography = currentTypography.copy(
            subtitle1 = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
            ),
            body2 = currentTypography.body1.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 1.sp
            ),
            button = currentTypography.button.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.em
            )
    )
    MaterialTheme(colors = dialogColors, typography = dialogTypography, content = content)
}