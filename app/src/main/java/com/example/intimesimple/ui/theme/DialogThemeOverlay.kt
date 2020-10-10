package com.example.intimesimple.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun INTimeDialogThemeOverlay(content: @Composable () -> Unit) {
    // Rally is always dark themed.
    val dialogColors = darkColors(
            primary = Color.White,
            surface = Color.White.copy(alpha = 0.12f).compositeOver(Color.Black),
            onSurface = Color.White
    )

    // Copy the current [Typography] and replace some text styles for this theme.
    val currentTypography = MaterialTheme.typography
    val dialogTypography = currentTypography.copy(
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