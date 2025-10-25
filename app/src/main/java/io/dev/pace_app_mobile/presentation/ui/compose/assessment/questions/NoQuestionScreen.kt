package io.dev.pace_app_mobile.presentation.ui.compose.assessment.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoQuestionScreen(navController: NavController) {
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_questions))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(BgApp),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "",
                showLeftButton = false,
                showRightButton = false
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Illustration
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(220.dp)
                    .padding(bottom = spacing.lg)
            )

            // Title
            Text(
                text = "No Questions Set Up",
                fontSize = sizes.titleFontSize,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.sm))

            // Subtitle
            Text(
                text = "It seems the assessment questions are not yet configured.\nPlease contact your administrator or try again later.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.primary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(spacing.xxl))

            // Retry or Back to Start Button
            CustomIconButton(
                icon = R.drawable.ic_refresh,
                text = "Try Again",
                width = 180.dp,
                backgroundColor = colors.primary.copy(alpha = 0.1f),
                contentColor = colors.primary,
                onClick = {
                    navController.navigate(Routes.START_ASSESSMENT_ROUTE) {
                        popUpTo(Routes.NO_QUESTION_ROUTE) { inclusive = true }
                    }
                }
            )
        }
    }
}
