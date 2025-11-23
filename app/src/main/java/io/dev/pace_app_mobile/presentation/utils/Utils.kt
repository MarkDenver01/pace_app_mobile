package io.dev.pace_app_mobile.presentation.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.dev.pace_app_mobile.domain.enums.HttpStatus
import timber.log.Timber


/**
 * Darken
 *
 * @param factor
 * @return
 */
fun Color.darken(factor: Float = 0.85f): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}

fun getHttpStatus(statusCode: Int): HttpStatus {
    return HttpStatus.fromCode(statusCode)
}

@Composable
fun ProgressDialog() {
    Dialog(
        onDismissRequest = { /* This dialog is not dismissible */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)), // Semi-transparent overlay
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun resolveLogoUrl(path: String?): String {
    val defaultLogo = "https://pace-app-backend.onrender.com/uploads/logos/default_logo.png"
    val backendBaseUrl = "https://pace-app-backend.onrender.com"

    if (path.isNullOrEmpty()) return defaultLogo

    return when {
        path.startsWith("http://") || path.startsWith("https://") -> path
        path.startsWith("/uploads/") -> "$backendBaseUrl$path"
        else -> path
    }
}

@Composable
fun UniversityLogo(path: String?) {
    Timber.e("xxxx path: $path")
    val logoUrl = resolveLogoUrl(path)

    AsyncImage(
        model = logoUrl,
        contentDescription = "University Logo",
        modifier = Modifier.size(300.dp)
    )
}

@Composable
inline fun <reified VM : ViewModel> sharedViewModel(
    navController: NavController,
    parentRoute: String = "root_graph"
): VM {
    // Use remember with the *backStackEntry.key* itself
    val parentEntry = navController.getBackStackEntry(parentRoute)
    return hiltViewModel(
        remember(parentEntry) { parentEntry }
    )
}

@Composable
fun UltraPremiumGradientBackground(content: @Composable () -> Unit) {

    // Infinite animation for smooth "breathing" gradient motion
    val infiniteTransition = rememberInfiniteTransition()

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Dynamic color stops (rotating between 3 curated premium orange tones)
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF8C00),   // bright orange
            Color(0xFFFFB347),   // soft pastel orange
            Color(0xFFFFD79B)    // warm cream-light orange
        ),
        start = Offset(0f, offset),
        end = Offset(offset, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        content()
    }
}

