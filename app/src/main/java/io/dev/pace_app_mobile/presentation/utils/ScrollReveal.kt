package io.dev.pace_app_mobile.presentation.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow

/**
 * Direction where the item should come from.
 */
enum class RevealFrom {
    BOTTOM,
    LEFT
}

/**
 * Simple scroll-reveal effect:
 * - fades in
 * - slides from bottom or left
 * - triggered once when the item enters the window area
 */
@Composable
fun Modifier.scrollReveal(
    index: Int,
    delayPerItem: Int = 110,
    from: RevealFrom = RevealFrom.BOTTOM
): Modifier {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = index * delayPerItem),
        label = "alphaReveal"
    )

    val translateY by animateFloatAsState(
        targetValue = if (visible || from != RevealFrom.BOTTOM) 0f else 40f,
        animationSpec = tween(durationMillis = 420, delayMillis = index * delayPerItem),
        label = "translateYReveal"
    )

    val translateX by animateFloatAsState(
        targetValue = if (visible || from != RevealFrom.LEFT) 0f else -40f,
        animationSpec = tween(durationMillis = 420, delayMillis = index * delayPerItem),
        label = "translateXReveal"
    )

    return this
        .onGloballyPositioned { coords ->
            // Very simple heuristic: when the top of the composable
            // is within the viewport height, mark it visible.
            val windowY = coords.positionInWindow().y
            val height = coords.size.height

            // Trigger when the item is not too far below the screen
            if (!visible && windowY < height * 8f) {
                visible = true
            }
        }
        .graphicsLayer {
            this.alpha = alpha
            this.translationY = translateY
            this.translationX = translateX
        }
}
