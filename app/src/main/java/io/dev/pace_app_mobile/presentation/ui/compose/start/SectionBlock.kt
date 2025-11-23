package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun SectionBlock(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            Text(
                content,
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start
            )
        }
    }
}
