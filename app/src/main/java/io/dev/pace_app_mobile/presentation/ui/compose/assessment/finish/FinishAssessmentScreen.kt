package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishAssessmentScreen(
    navController: NavController
) {
    val viewModel: AssessmentViewModel = sharedViewModel(navController)
    val navigateTo by viewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val colors = LocalAppColors.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgApp),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "",
                showLeftButton = true,
                showRightButton = false,
                leftIcon = R.drawable.ic_profile,
                onLeftClick = { viewModel.onProfileClick() }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Logo or illustration ---
            Image(
                painter = painterResource(id = R.drawable.ic_assessment_begin),
                contentDescription = "Assessment Result",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(spacing.md))
            Text(
                text = "You have completed your assessment!",
                fontSize = sizes.buttonFontSize,
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
            )
            Spacer(modifier = Modifier.height(spacing.lg))

            // --- View Recommended Course Button ---
            CustomDynamicButton(
                onClick = {  },
                content = "View Recommended Course",
                backgroundColor = colors.primary,
                pressedBackgroundColor = colors.pressed
            )
            Spacer(modifier = Modifier.height(spacing.md))

            // --- Re-Take Exam Button ---
            CustomDynamicButton(
                onClick = {  },
                content = "Re-Take Exam",
                backgroundColor = Color(0xFF4D9DDA), // Secondary color
                pressedBackgroundColor = colors.pressed
            )
        }
    }
}
