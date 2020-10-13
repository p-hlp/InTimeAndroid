package com.example.intimesimple.ui.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Modifier

// Credits to https://gist.github.com/bmc08gt
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun <T> AnimatedSwipeDismiss(
        modifier: Modifier = Modifier,
        item: T,
        background: @Composable (isDismissed: Boolean) -> Unit,
        content: @Composable (isDismissed: Boolean) -> Unit,
        directions: Set<DismissDirection> = setOf(DismissDirection.StartToEnd),
        enter: EnterTransition = expandVertically(),
        exit: ExitTransition = shrinkVertically(
                animSpec = tween(
                        durationMillis = 500,
                )
        ),
        onDismiss: (T) -> Unit
) {
    val dismissState = rememberDismissState()
    val isDismissed = dismissState.isDismissed(DismissDirection.StartToEnd)

    onCommit(dismissState.value) {
        if (dismissState.value == DismissValue.DismissedToEnd) {
            onDismiss(item)
        }
    }

    AnimatedVisibility(
            modifier = modifier,
            visible = !isDismissed,
            enter = enter,
            exit = exit
    ) {
        SwipeToDismiss(
                modifier = modifier,
                state = dismissState,
                directions = directions,
                background = { background(isDismissed) },
                dismissContent = { content(isDismissed) }
        )
    }
}