package io.dev.pace_app_mobile.presentation.ui.compose.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.CustomizationViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    navController: NavController
) {
    val aboutUsViewModel: AboutUsViewModel = sharedViewModel(navController)
    val customizationViewModel: CustomizationViewModel = sharedViewModel(navController)
    val assessmentViewModel: AssessmentViewModel = sharedViewModel(navController)
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current

    val aboutContent by aboutUsViewModel.aboutContent.collectAsState()
    val aboutResponse by customizationViewModel.aboutResponse.collectAsState()
    val loginResponse by assessmentViewModel.loginResponse.collectAsState()

    LaunchedEffect(Unit) {
        customizationViewModel.loadTheme(loginResponse?.studentResponse?.universityId ?: 0L)
    }

    LaunchedEffect(aboutResponse) {
        val aboutText = aboutResponse?.aboutText
        Timber.e("xxxxx about text: $aboutText" )
        aboutUsViewModel.setAboutText(aboutText ?: "")
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgApp),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "About Us",
                showLeftButton = true,
                leftIcon = R.drawable.ic_back,
                showRightButton = false,
                onLeftClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = loginResponse?.studentResponse?.universityName ?: "",
                fontSize = sizes.titleFontSize,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            AboutSection(
                title = "University Vision",
                content = aboutContent.vision
            )

            Divider(
                color = colors.primary.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = spacing.md)
            )

            AboutSection(
                title = "University Mission",
                content = aboutContent.mission
            )

            Divider(
                color = colors.primary.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = spacing.md)
            )

            AboutSection(
                title = "Core Values",
                content = aboutContent.coreValues.joinToString("\n") { "• $it" }
            )
        }
    }
}

@Composable
fun AboutSection(title: String, content: String) {
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = sizes.smallFontSize,
            color = colors.primary
        )

        Spacer(modifier = Modifier.height(spacing.sm))

        Text(
            text = content,
            color = Color.DarkGray,
            textAlign = TextAlign.Justify
        )
    }
}
