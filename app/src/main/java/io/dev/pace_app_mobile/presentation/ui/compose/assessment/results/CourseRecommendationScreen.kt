package io.dev.pace_app_mobile.presentation.ui.compose.assessment.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import io.dev.pace_app_mobile.domain.model.CourseRecommendation
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.AssessmentViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar

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

    val topCourses by viewModel.topCourses.collectAsState()

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Logo & Title ---
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
                    ) {
                        append("Recommended ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFCC4A1A),
                            fontSize = sizes.fontLargeSizeLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Course Result")
                    }
                },
                modifier = Modifier.padding(vertical = spacing.md)
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            // --- Course List ---
            if (topCourses.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(spacing.md),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(topCourses) { course ->
                       CourseCard(course)
                    }
                }
            } else {
                Text(
                    text = "No course results available.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CourseCard(course: CourseRecommendation) {
    val spacing = LocalAppSpacing.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
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
                text = "Result Percentage: ${"%.2f".format(course.matchPercentage)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4D9DDA)
            )

            Spacer(modifier = Modifier.height(spacing.sm))

            Text(
                text = "Message: ${course.recommendationMessage}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

