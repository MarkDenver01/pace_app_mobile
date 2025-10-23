package io.dev.pace_app_mobile.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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