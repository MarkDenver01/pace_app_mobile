package io.dev.pace_app_mobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.dev.pace_app_mobile.presentation.ui.compose.start.StartScreen

fun NavGraphBuilder.startGraph(navController: NavController) {
    composable(Routes.START_ROUTE) {
        StartScreen(navController = navController)
    }
}

fun NavGraphBuilder.TitleGraph(navController: NavController) {
    // TODO
}