package io.dev.pace_app_mobile.presentation.ui.compose.assessment.done

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.domain.model.StudentResponse
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
fun QuestionCompletedScreen(
    navController: NavController,
) {
    val assessmentViewModel: AssessmentViewModel = sharedViewModel(navController)
    val startViewModel: StartViewModel = sharedViewModel(navController)
    val navigateTo by assessmentViewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val colors = LocalAppColors.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showOldNewStudentDialog by assessmentViewModel.showOldNewStudentDialog.collectAsState()
    val userType by startViewModel.userTypeFlow.collectAsState()
    val loginResponse by assessmentViewModel.loginResponse.collectAsState()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            assessmentViewModel.resetNavigation()
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
                            append("")
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
                    when (userType) {
                        UserType.GUEST -> {

                        }

                        else -> {
                            val studentRequest = StudentAssessmentRequest(
                                email = loginResponse?.studentResponse?.email,
                                userName = loginResponse?.studentResponse?.userName,
                                assessmentStatus = "DONE",
                                universityId = loginResponse?.studentResponse?.universityId,
                                enrolledUniversity = loginResponse?.studentResponse?.universityName
                            )
                            assessmentViewModel.setStudentAssessment(studentRequest)
                        }
                    }

                    assessmentViewModel.onViewResultsClick(userType)
                },
                content = "See Results",
                backgroundColor = colors.primary,
                pressedBackgroundColor = colors.pressed
            )
        }
    }

    if (showOldNewStudentDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Student Registration",
            message = "Are you an old student or new student?",
            show = showOldNewStudentDialog,
            onConfirm = {
                startViewModel.setUserType(UserType.NEW_WITH_GUEST)
                assessmentViewModel.confirmNewStudent()
            },   // "New Student"
            onDismiss = {
                startViewModel.setUserType(UserType.OLD_WITH_GUEST)
                assessmentViewModel.confirmOldStudent()
            },   // "Old Student"
            confirmText = "New Student",
            dismissText = "Old Student"
        )
    }
}
