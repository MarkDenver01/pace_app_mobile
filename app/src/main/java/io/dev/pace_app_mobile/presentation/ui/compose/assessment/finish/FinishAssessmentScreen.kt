package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import android.widget.Toast
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartViewModel
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishAssessmentScreen(
    navController: NavController
) {
    val viewModel: AssessmentViewModel = sharedViewModel(navController)
    val startViewModel: StartViewModel = sharedViewModel(navController)
    val navigateTo by viewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val colors = LocalAppColors.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showRetakeDialog by remember { mutableStateOf(false) }
    val loginResponse by viewModel.loginResponse.collectAsState()
    val deleteSuccess by viewModel.deleteStudentAssessment.collectAsState()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }


    // SweetAlertDialog
    RetakeAssessmentDialog(
        showDialog = showRetakeDialog,
        onConfirmRetake = {
            showRetakeDialog = false
            val email = loginResponse?.studentResponse?.email.orEmpty()
            viewModel.deleteStudentAssessment(email)
        },
        onDismissRetake = {
            showRetakeDialog = false
        }
    )

    // Observe deletion result
    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            startViewModel.setUserType(UserType.DEFAULT)
            // Reset ViewModel state if needed
            viewModel.resetDeleteState() // optional, see below

            // Navigate to assessment start
            navController.navigate(Routes.START_ASSESSMENT_ROUTE) {
                popUpTo(Routes.FINISH_ASSESSMENT_ROUTE) { inclusive = true }
            }
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
                onClick = {
                    showRetakeDialog = true
                },
                content = "Re-Take Exam",
                backgroundColor = Color(0xFF4D9DDA), // Secondary color
                pressedBackgroundColor = colors.pressed
            )
        }
    }


    RetakeAssessmentDialog(
        showDialog = showRetakeDialog,
        onConfirmRetake = {
            showRetakeDialog = false
            val email = loginResponse?.studentResponse?.email ?: ""
            viewModel.deleteStudentAssessment(email)
        },
        onDismissRetake = {
            showRetakeDialog = false
        }
    )
}


@Composable
fun RetakeAssessmentDialog(
    showDialog: Boolean,
    onConfirmRetake: () -> Unit,
    onDismissRetake: () -> Unit
) {
    SweetAlertDialog(
        type = AlertType.QUESTION,
        title = "Re-take Assessment",
        message = "Do you want to re-take the assessment?",
        show = showDialog,
        onConfirm = {
            onConfirmRetake()
        },
        onDismiss = {
            onDismissRetake()
        },
        confirmText = "Yes",
        dismissText = "No",
        isSingleButton = false
    )
}

