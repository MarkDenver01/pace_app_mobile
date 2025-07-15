package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton

@Composable
fun StartScreen(
    navController: NavController,
    viewModel: StartViewModel = hiltViewModel()
) {
    // Navigation observer
    LaunchedEffect(Unit) {
        viewModel.navigateToTitle.collect {
            navController.navigate(Routes.TITLE_ROUTE) {
                popUpTo(Routes.START_ROUTE) { inclusive = true }
            }
        }
    }

    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BgApp)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Logo Block ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "PACE Logo",
                    modifier = Modifier.size(300.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- Get Started Button ---
            CustomDynamicButton(
                onClick = { viewModel.onStartClick() },
                content = "Get Started"
            )
        }
    }
}
