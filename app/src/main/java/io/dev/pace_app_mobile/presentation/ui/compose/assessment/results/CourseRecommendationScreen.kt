package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import io.dev.pace_app_mobile.domain.model.CourseRecommendation
import io.dev.pace_app_mobile.presentation.theme.*
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRecommendedResultScreen(
    navController: NavController,
    viewModel: AssessmentViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = LocalAppColors.current
    val topCourses by viewModel.topCourses.collectAsState()

    var selectedCourse by remember { mutableStateOf<CourseRecommendation?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    // Fetch course recommendations from backend
    LaunchedEffect(Unit) {
        if (topCourses.isEmpty()) {
            viewModel.fetchCourseRecommendation()
        }
    }

    // Handle navigation
    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    val top3Courses = remember(topCourses) {
        topCourses.sortedByDescending { it.matchPercentage }.take(3)
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
        EnrollmentDialog(
            course = selectedCourse!!,
            isChecked = isChecked,
            onCheckedChange = { isChecked = it },
            onSubmit = {
                showDialog = false
                showResultDialog = true
            },
            onDismiss = { showDialog = false }
        )
    }

    // Step 2: Result dialog
    if (showResultDialog) {
        SweetAlertDialog(
            type = if (isChecked) AlertType.SUCCESS else AlertType.WARNING,
            title = if (isChecked) "Enrollment Information" else "Thank You!",
            message = if (isChecked)
                "You may visit your school’s official page:\nhttps://www.exampleuniversity.edu/enroll"
            else
                "Thank you for taking the assessment!",
            show = showResultDialog,
            onConfirm = { showResultDialog = false },
            isSingleButton = true,
            confirmText = "Close"
        )
    }
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
fun EnrollmentDialog(
    course: CourseRecommendation,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onSubmit) { Text("Submit") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(text = "Interested in ${course.courseName}?") },
        text = {
            Column {
                Text("Would you like to enroll in this school?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
                    Text("Yes, I want to enroll.")
                }
            }
        }
    )
}
