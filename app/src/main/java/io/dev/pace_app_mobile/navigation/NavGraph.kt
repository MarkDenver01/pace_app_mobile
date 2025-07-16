package io.dev.pace_app_mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginScreen
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartScreen
import io.dev.pace_app_mobile.presentation.ui.compose.title.TitleScreen

fun NavGraphBuilder.startGraph(navController: NavController) {
    composable(Routes.START_ROUTE) {
        StartScreen(navController = navController)
    }
}

fun NavGraphBuilder.titleGraph(navController: NavController) {
    composable(Routes.TITLE_ROUTE) {
        TitleScreen(navController = navController)
    }
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable(Routes.LOGIN_ROUTE) {
        LoginScreen(navController = navController)
    }
}