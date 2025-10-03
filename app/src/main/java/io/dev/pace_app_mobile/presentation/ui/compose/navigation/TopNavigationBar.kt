package io.dev.pace_app_mobile.presentation.ui.compose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.utils.responsiveDp
import io.dev.pace_app_mobile.presentation.utils.responsiveSp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavController,
    title: String,
    showLeftButton: Boolean = true,
    showRightButton: Boolean = true,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues().calculateTopPadding()

    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.primary) // Color fills behind status bar
    ) {
        Spacer(modifier = Modifier.height(statusBarHeight))

        Divider(
            color = Color.White.copy(alpha = 0.5f), // or Color.Gray, or any contrast color
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp) // Adjust as needed
                .background(colors.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Icon
            if (showLeftButton) {
                IconButton(onClick = onLeftClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh), // your custom drawable
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White // Or Color.Unspecified if you want original colors
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Title
            Text(
                text = title,
                fontSize = responsiveSp(20f),
                color = Color.White,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            // Right Icon
            if (showRightButton) {
                IconButton(onClick = onRightClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close), // your custom drawable
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White // Or Color.Unspecified if you want original colors
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}

