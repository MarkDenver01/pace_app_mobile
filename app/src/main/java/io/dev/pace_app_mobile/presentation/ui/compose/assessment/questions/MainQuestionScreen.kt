package io.dev.pace_app_mobile.presentation.ui.compose.assessment.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.AlertConfirmationDialog
import io.dev.pace_app_mobile.presentation.utils.CustomIconButton
import io.dev.pace_app_mobile.presentation.utils.ProgressHeader
import io.dev.pace_app_mobile.presentation.utils.YesNoButtonGroup


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainQuestionScreen(
    navController: NavController,
    viewModel: AssessmentViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val answeredList by viewModel.answers.collectAsState()
    val colors = LocalAppColors.current

    val totalQuestions = viewModel.totalQuestions

    // Category progress is derived reactively from currentQuestion
    val categoryProgress = remember(currentQuestion) {
        viewModel.getCurrentCategoryProgress()
    }

    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showRetryDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var userAnswers: List<AnsweredQuestionRequest>

    LaunchedEffect(Unit) {
        viewModel.fetchQuestions()
    }

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    LaunchedEffect(totalQuestions) {
        if (totalQuestions == 0) {
            navController.navigate(Routes.NO_QUESTION_ROUTE) {
                popUpTo(Routes.START_ASSESSMENT_ROUTE) { inclusive = true }
            }
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
                showLeftButton = true,
                showRightButton = true,
                onLeftClick = {
                    showRetryDialog = true
                },
                onRightClick = {
                    showExitDialog = true
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        // Retry Dialog
        if (showRetryDialog) {
            AlertConfirmationDialog(
                message = "Are you sure you want to retry?",
                onConfirm = {
                    showRetryDialog = false
                    // Handle retry logic
                    viewModel.resetAssessment()
                },
                onCancel = {
                    showRetryDialog = false
                }
            )
        }


        if (showExitDialog) {
            AlertConfirmationDialog(
                message = "Are you sure you want to exit?",
                onConfirm = {
                    showExitDialog = false
                    // Handle exit logic
                },
                onCancel = {
                    showExitDialog = false
                }
            )
        }


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProgressHeader(
                    currentIndex = currentIndex + 1,
                    totalQuestions = totalQuestions
                )
            }

            Spacer(modifier = Modifier.height(spacing.sm))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Reactive category progress
                Text(
                    text = "${currentQuestion.category.displayName} (${categoryProgress.first} of ${categoryProgress.second})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Question ${currentIndex + 1}",
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        append("Kindly choose ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) { append("YES") }
                        append(" or ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) { append("NO") }
                        append(" for the following question.")
                    },
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary,
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                Divider(
                    color = Color.DarkGray.copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Image(
                    painter = painterResource(id = currentQuestion.imageResId),
                    contentDescription = "Assessment Image",
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currentQuestion.text,
                    textAlign = TextAlign.Center,
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary,
                )

                Spacer(modifier = Modifier.height(spacing.xxl))

                YesNoButtonGroup(
                    selected = selectedAnswer,
                    onSelect = { viewModel.onAnswerClick(it) }
                )

                Spacer(modifier = Modifier.height(spacing.xxl))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    val isLastQuestion = currentIndex == totalQuestions - 1
                    CustomIconButton(
                        icon = if (isLastQuestion) R.drawable.ic_check else R.drawable.ic_next,
                        iconTint = if (isLastQuestion) Color(0xFF2E7D32) else Color.DarkGray,
                        iconSize = 32.dp,
                        onClick = {
                            if (isLastQuestion) viewModel.onCompletedAssessment()
                            else viewModel.goToNextQuestion()
                        },
                        text = if (isLastQuestion) "Submit" else null,
                        enabled = selectedAnswer != null,
                        width = if (isLastQuestion) 120.dp else 50.dp,
                        backgroundColor = Color.Red.copy(alpha = 0.1f),
                        contentColor = Color.Red,
                    )
                }

                Spacer(modifier = Modifier.height(spacing.xxl))
            }
        }
    }
}
