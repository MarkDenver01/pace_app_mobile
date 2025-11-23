package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 24,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
    ) {

        // BACKGROUND GLASS LAYER (BEHIND)
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(18.dp) // frosted effect BEHIND content
                .background(Color.White.copy(alpha = 0.12f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
        )

        // MAIN CONTENT (FRONT)
        Box(
            modifier = Modifier
                .padding(18.dp)
        ) {
            content()
        }
    }
}
