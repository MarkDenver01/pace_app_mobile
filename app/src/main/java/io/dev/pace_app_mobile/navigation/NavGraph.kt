package io.dev.pace_app_mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.done.QuestionCompletedScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.questions.MainQuestionScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.results.CourseRecommendedResultScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.start.StartExamScreen
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginScreen
import io.dev.pace_app_mobile.presentation.ui.compose.signup.SignUpScreen
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartScreen
import io.dev.pace_app_mobile.presentation.ui.compose.title.TitleScreen

fun NavGraphBuilder.startGraph(navController: NavController) {
    composable(Routes.START_ROUTE) {
        StartScreen(navController = navController)
    }
}

fun NavGraphBuilder.titleGraph(
    navController: NavController,
    universityId: String?,
    dynamicToken: String
) {
    composable(Routes.TITLE_ROUTE) {
        TitleScreen(navController = navController)
    }

    composable(Routes.SIGN_UP_ROUTE) {
        SignUpScreen(navController, universityId, dynamicToken)
    }

    composable(Routes.LOGIN_ROUTE) {
        LoginScreen(navController, universityId, dynamicToken)
    }
}

fun NavGraphBuilder.assessmentGraph(navController: NavController) {
    composable(Routes.START_ASSESSMENT_ROUTE) {
        StartExamScreen(navController)
    }

    composable(Routes.QUESTION_ROUTE) {
        MainQuestionScreen(navController)
    }

    composable(Routes.QUESTION_COMPLETED_ROUTE) {
        QuestionCompletedScreen(navController)
    }

    composable(Routes.COURSE_RECOMMENDATION_ROUTE) {
        CourseRecommendedResultScreen(navController)
    }
}