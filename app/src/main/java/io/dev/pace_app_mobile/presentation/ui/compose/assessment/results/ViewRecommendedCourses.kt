package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.RecommendedCourseResponse
import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRecommendedCoursesScreen(
    navController: NavController
) {
    val assessmentViewModel: AssessmentViewModel = sharedViewModel(navController)
    val studentResponse by assessmentViewModel.studentAssessResponse.collectAsState()
    val loginResponse by assessmentViewModel.loginResponse.collectAsState()

    val spacing = 16.dp

    // 🔹 Call assessment API only once when screen is first loaded
    LaunchedEffect(loginResponse) {
        loginResponse?.studentResponse?.let { student ->
            val selectedUniversityId = student.universityId ?: 0L
            val selectedEmail = student.email.orEmpty()

            if (selectedUniversityId != 0L && selectedEmail.isNotEmpty()) {
                assessmentViewModel.getStudentAssessment(selectedUniversityId, selectedEmail)
            } else {
                Timber.e("University ID or email missing. Skipping fetch.")
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
                showLeftButton = false,
                showRightButton = false
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        studentResponse?.let { response ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                item {
                    Spacer(modifier = Modifier.height(spacing))

                    // --- Header Image ---
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_result),
                            contentDescription = "Assessment Result",
                            modifier = Modifier.size(160.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(spacing))

                    // --- University & Enrollment Info ---
                    ResultHeaderSection(response)

                    Spacer(modifier = Modifier.height(spacing))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(spacing / 2))

                    Text(
                        text = "Recommended Courses",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0170C1)
                    )
                }

                // --- Recommended Courses List ---
                items(response.recommendedCourses) { course ->
                    RecommendedCourseCard(course)
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        } ?: run {
            // --- Loading or Empty State ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No assessment result available.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ResultHeaderSection(response: StudentAssessmentResponse) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Enrolled University:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0170C1)
            )
            Text(
                text = response.enrolledUniversity ?: "Unknown University",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enrollment Status: ${response.enrollmentStatus ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            val statusColor =
                if (response.assessmentStatus == "DONE") Color(0xFF4CAF50) else Color.Gray

            Text(
                text = "Assessment Status: ${response.assessmentStatus ?: "Pending"}",
                style = MaterialTheme.typography.bodyMedium,
                color = statusColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val dateText = response.createdDateTime
                ?.takeIf { it.isNotEmpty() }
                ?.take(19)
                ?.replace("T", " ")
                ?: "Not Available"

            Text(
                text = "Date Completed: $dateText",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
private fun RecommendedCourseCard(course: RecommendedCourseResponse) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = course.courseDescription ?: "Untitled Course",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0170C1)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Match Result: ${"%.2f".format(course.assessmentResult ?: 0.0)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4D9DDA)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = course.assessmentDescription ?: "No assessment details available.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Possible Careers:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF0170C1)
            )

            Spacer(modifier = Modifier.height(4.dp))
            if (course.careers.isNullOrEmpty()) {
                Text(
                    text = "No career information available.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } else {
                course.careers.forEach { career ->
                    Text(
                        text = "- ${career.careerName ?: "Unknown Career"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

