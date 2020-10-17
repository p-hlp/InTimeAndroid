package com.example.intimesimple.ui.composables

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import com.example.intimesimple.data.local.Workout
import com.example.intimesimple.ui.theme.Green500
import timber.log.Timber

@ExperimentalMaterialApi
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
        // https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key
        key(item.id){
            val dismissState = rememberDismissState()
            Timber.d("DismissState: ${dismissState.value}")
            onCommit(dismissState.value){
                if(dismissState.value == DismissValue.DismissedToEnd ){
                    Timber.d("onSwipe() - Dismissing WorkoutID: ${item.id}")
                    onSwipe(item)
                }
            }

            // Fixed with alpha05
            SwipeToDismiss(
                modifier = modifier,
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd),
                background = {
                    WorkoutItemDismissBackground()
                }
            ){
                WorkoutItem(
                    workout = item,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun WorkoutListAnimatedContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    items: List<Workout>,
    onSwipe: (Workout) -> Unit,
    onClick: (Workout) -> Unit
){
    LazyColumnFor(
        modifier = modifier.padding(innerPadding),
        items = items,
    ) { item ->
        // https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#key
        key(item.id){
            AnimatedSwipeDismiss(
                item = item,
                background = {
                    WorkoutItemDismissBackground()
                },
                content = {
                    WorkoutItem(
                        workout = item,
                        onClick = onClick
                    )
                },
                onDismiss = {
                    onSwipe(item)
                }
            )
        }
    }
}