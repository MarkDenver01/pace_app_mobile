package io.dev.pace_app_mobile.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    navController: NavController,
    title: String,
    showLeftButton: Boolean = true,
    showRightButton: Boolean = true,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    leftIcon: Int? = R.drawable.ic_refresh,
    rightIcon: Int? = R.drawable.ic_close
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.primary)
    ) {
        // Status bar spacer
        Spacer(modifier = Modifier.height(statusBarHeight))

        Divider(
            color = Color.White.copy(alpha = 0.5f),
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(colors.primary)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LEFT ICON
            if (showLeftButton && leftIcon != null) {
                IconButton(onClick = onLeftClick) {
                    Icon(
                        painter = painterResource(id = leftIcon),
                        contentDescription = "Left Icon",
                        modifier = Modifier.size(responsiveDp(24f)),
                        tint = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(responsiveDp(48f)))
            }

            // TITLE
            Text(
                text = title,
                fontSize = responsiveSp(20f),
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                maxLines = 1
            )

            // RIGHT ICON
            if (showRightButton && rightIcon != null) {
                IconButton(onClick = onRightClick) {
                    Icon(
                        painter = painterResource(id = rightIcon),
                        contentDescription = "Right Icon",
                        modifier = Modifier.size(responsiveDp(24f)),
                        tint = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(responsiveDp(48f)))
            }
        }
    }
}
