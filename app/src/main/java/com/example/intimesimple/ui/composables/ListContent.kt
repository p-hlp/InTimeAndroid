package com.example.intimesimple.ui.composables

import androidx.compose.animation.animate
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.theme.Green500
import timber.log.Timber

@Composable
fun WorkoutListContent(
        modifier: Modifier = Modifier,
        innerPadding: PaddingValues,
        items: List<Workout>,
        onSwipe: (Workout) -> Unit,
        onClick: (Workout) -> Unit
) {
    LazyColumnFor(
            modifier = modifier.padding(innerPadding),
            items = items,
    ) { item ->
        AnimatedSwipeDismiss(
                item = item,
                background = { isDismissed ->
                    Surface(
                            modifier = modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Green500,
                            shape = MaterialTheme.shapes.medium,
                    ) {
                        Column(modifier) {
                            Surface(
                                    color = Color.Red,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxHeight()
                            ) {
                                Box{
                                    val alpha = animate(if (isDismissed) 0f else 1f)
                                    Icon(
                                            Icons.Filled.Delete,
                                            tint = Color.White.copy(alpha = alpha),
                                            modifier = Modifier
                                                    .align(Alignment.CenterStart)
                                                    .padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                content = {
                    WorkoutItem(workout = item, onClick = onClick)
                },
                onDismiss = {
                    Timber.d("onDismiss: Workout - ID: ${it.id}")
                    onSwipe(it)
                }
        )
    }
}