package com.example.intimesimple.ui.composables

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.intimesimple.ui.theme.INTimeTheme

@Composable
internal fun ThemedPreview(
        darkTheme: Boolean = false,
        children: @Composable () -> Unit
) {
    INTimeTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}