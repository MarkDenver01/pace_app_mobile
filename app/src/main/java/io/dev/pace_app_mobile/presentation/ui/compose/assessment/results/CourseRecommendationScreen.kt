package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.domain.model.CareerRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendation
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.RecommendedCourseRequest
import io.dev.pace_app_mobile.domain.model.RecommendedCourseResponse
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.presentation.theme.*
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartViewModel
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel
import timber.log.Timber
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRecommendedResultScreen(
    navController: NavController
) {
    val assessmentViewModel: AssessmentViewModel = sharedViewModel(navController)
    val startViewModel: StartViewModel = sharedViewModel(navController)
    val navigateTo by assessmentViewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = LocalAppColors.current
    val topCourses by assessmentViewModel.topCourses.collectAsState()

    var selectedCourse by remember { mutableStateOf<CourseRecommendation?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    val userType by startViewModel.userTypeFlow.collectAsState()
    val selectedUniversity by assessmentViewModel.university.collectAsState()
    val studentAssessmentRequest by assessmentViewModel.studentAssessmentRequest.collectAsState()
    val studentResponse by assessmentViewModel.studentAssessResponse.collectAsState()
    val loginResponse by assessmentViewModel.loginResponse.collectAsState()

    // Fetch course recommendations from backend
    LaunchedEffect(Unit) {
        if (topCourses.isEmpty()) {
            assessmentViewModel.fetchCourseRecommendation()
        }
    }

    // Handle navigation
    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            assessmentViewModel.resetNavigation()
        }
    }

    val top3Courses = remember(topCourses) {
        topCourses.sortedByDescending { it.matchPercentage }.take(3)
    }

    LaunchedEffect(topCourses) {
        if (topCourses.isNotEmpty()) {
            val top3Courses = topCourses
                .sortedByDescending { it.matchPercentage }
                .take(3)

            val mappedCourses = top3Courses.map { course ->
                RecommendedCourseRequest(
                    courseDescription = course.courseDescription,
                    assessmentResult = course.matchPercentage,
                    resultDescription = course.recommendationMessage,
                    careers = course.possibleCareers.map { careerName ->
                        CareerRequest(careerName = careerName)
                    }
                )
            }

            assessmentViewModel.setExamResult(
                StudentAssessmentRequest(recommendedCourseRequests = mappedCourses)
            )
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
                showLeftButton = false,
                showRightButton = false
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        // 🔹 Entire screen is scrollable now
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_result),
                contentDescription = "Assessment Result",
                modifier = Modifier.size(180.dp)
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF4D9DDA),
                            fontSize = sizes.fontLargeSizeLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) { append("Top 3 ") }
                    withStyle(
                        style = SpanStyle(
                            color = colors.primary,
                            fontSize = sizes.fontLargeSizeLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) { append("Course Recommendations") }
                },
                modifier = Modifier.padding(vertical = spacing.md)
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            if (top3Courses.isNotEmpty()) {
                top3Courses.forEach { course ->
                    CourseCard(
                        course = course,
                        onClick = {
                            selectedCourse = course
                            showDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(spacing.md))
                }
            } else {
                Text(
                    text = "No course recommendations available.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(80.dp)) // bottom space for safety
        }
    }

    // Step 1: Enrollment prompt
    if (showDialog && selectedCourse != null) {
        SweetEnrollmentDialog(
            course = selectedCourse!!,
            isChecked = isChecked,
            show = showDialog,
            onCheckedChange = { isChecked = it },
            onSubmit = { otherSchool ->
                val enrolledUniversity: String
                val enrollmentStatus: String

                if (isChecked) {
                    enrolledUniversity = loginResponse?.studentResponse?.universityName.orEmpty()
                    enrollmentStatus = "Same school"
                } else {
                    enrolledUniversity = otherSchool.orEmpty()
                    enrollmentStatus = "New school"
                }

                // Combine existing recommended courses + user info + enrollment data
                val finalRequest = studentAssessmentRequest?.copy(
                    userName = loginResponse?.studentResponse?.userName,
                    email = loginResponse?.studentResponse?.email,
                    universityId = loginResponse?.studentResponse?.universityId,
                    enrolledUniversity = enrolledUniversity,
                    enrollmentStatus = enrollmentStatus,
                    assessmentStatus = "DONE"
                )

                if (finalRequest != null) {
                    assessmentViewModel.saveStudentAssessment(finalRequest)
                } else {
                    Timber.e("studentAssessmentRequest is null — ensure topCourses loaded first")
                }

                showDialog = false
                showResultDialog = true
            },
            onDismiss = { showDialog = false }
        )
    }


    // Step 2: Result dialog
    ResultDialogContent(
        showResultDialog = showResultDialog,
        isChecked = isChecked,
        loginResponse = loginResponse, // your actual loginResponse object
        assessmentViewModel = assessmentViewModel,
        onDismiss = { showResultDialog = false }
    )
}

@Composable
fun CourseCard(course: CourseRecommendation, onClick: () -> Unit) {
    val spacing = LocalAppSpacing.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Course: ${course.courseName}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0170C1)
            )
            Spacer(modifier = Modifier.height(spacing.sm))
            Text(
                text = "Description: ${course.courseDescription}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(spacing.sm))
            Text(
                text = "Result: ${course.recommendationMessage}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(spacing.sm))
            Text(
                text = "Possible Careers: ${
                    if (course.possibleCareers.isNullOrEmpty())
                        "No available careers yet"
                    else
                        course.possibleCareers.joinToString(", ")
                }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(spacing.sm))
            Text(
                text = "Result Match: ${"%.2f".format(course.matchPercentage)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4D9DDA)
            )
        }
    }
}

@Composable
fun SweetEnrollmentDialog(
    course: CourseRecommendation,
    isChecked: Boolean,
    show: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onSubmit: (String?) -> Unit, // Pass otherSchool text if applicable
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    var otherSchool by remember { mutableStateOf("") } // State for the TextField

    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            AnimatedVisibility(
                visible = show,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.85f),
                exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.8f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.5f))
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 6.dp,
                        shadowElevation = 12.dp,
                        color = Color.White,
                        modifier = Modifier
                            .animateContentSize(animationSpec = tween(300))
                            .widthIn(min = 320.dp, max = 500.dp) // <-- Increased width
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            // --- Lottie Animation ---
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.question)
                            )
                            val progress by animateLottieCompositionAsState(
                                composition = composition,
                                iterations = LottieConstants.IterateForever
                            )
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .size(120.dp)
                                    .graphicsLayer {
                                        alpha = 0.9f
                                        scaleX = 1.1f
                                        scaleY = 1.1f
                                    }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Interested in ${course.courseName}?",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Would you like to enroll in this school?",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = onCheckedChange
                                )
                                Text(
                                    "Yes, I want to enroll.",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // --- Conditionally show TextField ---
                            if (!isChecked) {
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = otherSchool,
                                    onValueChange = { otherSchool = it },
                                    label = { Text("Other School") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = onDismiss,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, Color.LightGray),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    onClick = { onSubmit(if (isChecked) null else otherSchool) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colors.primary
                                    ),
                                    contentPadding = PaddingValues(vertical = 10.dp)
                                ) {
                                    Text(
                                        "Submit",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultDialogContent(
    showResultDialog: Boolean,
    isChecked: Boolean,
    loginResponse: LoginResponse?,
    assessmentViewModel: AssessmentViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    if (!showResultDialog) return

    // Define link
    val universityUrl = "https://www.exampleuniversity.edu/enroll"

    // Annotated clickable text
    val annotatedText = buildAnnotatedString {
        if (isChecked) {
            append("You may visit your school’s official page:\n")
            pushStringAnnotation(tag = "URL", annotation = universityUrl)
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF4D9DDA),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(universityUrl)
            }
            pop()
        } else {
            append("Thank you for taking the assessment!")
        }
    }

    SweetAlertDialog(
        type = if (isChecked) AlertType.SUCCESS else AlertType.WARNING,
        title = if (isChecked) "Enrollment Information" else "Thank You!",
        message = "", // Leave empty to handle custom Compose text below
        show = showResultDialog,
        isSingleButton = true,
        confirmText = "Close",
        onConfirm = {
            onDismiss()
            if (!isChecked) {
                val selectedUniversityId = loginResponse?.studentResponse?.universityId ?: 0L
                val selectedEmail = loginResponse?.studentResponse?.email.orEmpty()
                assessmentViewModel.getStudentAssessment(selectedUniversityId, selectedEmail)
            }
        }
    )

    // Overlay the clickable message under the dialog
    if (showResultDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                ClickableText(
                    text = annotatedText,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(
                            tag = "URL",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let { annotation ->
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                context.startActivity(intent)
                            }
                    }
                )
            }
        }
    }
}






