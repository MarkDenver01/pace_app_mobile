package io.dev.pace_app_mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.done.QuestionCompletedScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.questions.MainQuestionScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.questions.NoQuestionScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.results.CourseRecommendedResultScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.results.FinishAssessmentScreen
import io.dev.pace_app_mobile.presentation.ui.compose.assessment.start.StartExamScreen
import io.dev.pace_app_mobile.presentation.ui.compose.email_verification.EmailVerificationScreen
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginScreen
import io.dev.pace_app_mobile.presentation.ui.compose.profile.UserProfileScreen
import io.dev.pace_app_mobile.presentation.ui.compose.signup.SignUpScreen
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartScreen
import io.dev.pace_app_mobile.presentation.ui.compose.title.TitleScreen
import okhttp3.Route

fun NavGraphBuilder.startGraph(
    navController: NavController,
    universityId: String? = null,
    dynamicToken: String? = null
) {
    composable(Routes.START_ROUTE) {
        StartScreen(
            navController = navController,
            universityId = universityId,
            dynamicToken = dynamicToken
        )
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

    composable(
        route = Routes.SIGN_UP_WITH_ARG,
        arguments = listOf(
            navArgument("isOldStudent") { type = NavType.BoolType }
        )
    ) { backStackEntry ->
        val isOldStudent = backStackEntry.arguments?.getBoolean("isOldStudent") ?: false
        SignUpScreen(navController = navController, isOldStudent = isOldStudent)
    }

    composable(Routes.LOGIN_ROUTE) {
        LoginScreen(navController, universityId, dynamicToken)
    }

    composable(
        route = Routes.EMAIL_VERIFICATION_WITH_ARG,
        arguments = listOf(navArgument("email") { type = NavType.StringType })
    ) { backStackEntry ->
        val email = backStackEntry.arguments?.getString("email") ?: ""
        EmailVerificationScreen(navController, email)
    }
}

fun NavGraphBuilder.assessmentGraph(navController: NavController) {
    composable(Routes.START_ASSESSMENT_ROUTE) {
        StartExamScreen(navController)
    }

    composable(Routes.QUESTION_ROUTE) {
        MainQuestionScreen(navController)
    }

    composable(Routes.NO_QUESTION_ROUTE) {
        NoQuestionScreen(navController)
    }

    composable(Routes.QUESTION_COMPLETED_ROUTE) {
        QuestionCompletedScreen(navController)
    }

    composable(Routes.COURSE_RECOMMENDATION_ROUTE) {
        CourseRecommendedResultScreen(navController)
    }

    composable(Routes.USER_PROFILE_ROUTE) {
        UserProfileScreen(navController)
    }

    composable(Routes.FINISH_ASSESSMENT_ROUTE) {
        FinishAssessmentScreen(navController)
    }
}