package io.dev.pace_app_mobile.presentation.ui.compose.assessment.done

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.AssessmentResultDialog
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionCompletedScreen(
    navController: NavController,
    viewModel: AssessmentViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
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
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Logo Block ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_assessment_done),
                    contentDescription = "Assessment",
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF4D9DDA), // Blue
                                fontSize = sizes.fontLargeSizeLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Assessment ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFCC4A1A), // Orange
                                fontSize = sizes.fontLargeSizeLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Done")
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp))

            // --- let's begin ---
            CustomDynamicButton(
                onClick = {
                    viewModel.onViewResultsClick()
                },
                content = "See Results",
                backgroundColor = Color(0xFF0170C1),
                pressedBackgroundColor = Color(0xFF4D9DDA)
            )
        }
    }
}