package com.example.intimesimple.ui.composables

import androidx.compose.animation.animate
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListContent(
        modifier: Modifier = Modifier,
        innerPadding: PaddingValues,
        items: List<T>,
        onSwipe: (T) -> Unit,
        onClick: (T) -> Unit
) {
    LazyColumnFor(
            modifier = modifier.padding(innerPadding),
            items = items,
    ) { item ->
        AnimatedSwipeDismiss(
                item = item,
                background = { isDismissed ->
                    Box(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                    ) {
                        val alpha = animate( if (isDismissed) 0f else 1f)
                        Icon(Icons.Filled.Delete, tint = Color.White.copy(alpha = alpha))
                    }
                },
                content = { /* your item cell (feed your on click here) */ },
                onDismiss = { onSwipe(it) })
    }
}