package io.dev.pace_app_mobile.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.navigation.assessmentGraph
import io.dev.pace_app_mobile.navigation.startGraph
import io.dev.pace_app_mobile.navigation.titleGraph
import io.dev.pace_app_mobile.presentation.theme.Pace_app_mobileTheme
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var universityId: String? = null
    private var dynamicToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Extract dynamic link params
        intent?.data?.let { uri ->
            universityId = uri.getQueryParameter("universityId")
            dynamicToken = uri.getQueryParameter("token").toString()
            Timber.d("Dynamic link → universityId: $universityId, token: $dynamicToken")
        }

        // Handle OAuth redirects
        handleAuthRedirect(intent)

        setContent {
            Pace_app_mobileTheme {
                val navController = rememberNavController()

                // Decide startDestination
                val startDestination = when {
                    // 1. From dynamic link
                    !universityId.isNullOrEmpty() && dynamicToken.isNotEmpty() -> {
                        Routes.TITLE_ROUTE
                    }
                    // 2. Already logged in
                    loginViewModel.isUserLoggedIn() -> {
                        Routes.START_ASSESSMENT_ROUTE
                    }
                    // 3. Default
                    else -> {
                        Routes.START_ROUTE
                    }
                }

                Timber.d("Navigation startDestination → $startDestination")

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    startGraph(navController)
                    titleGraph(navController, universityId, dynamicToken)
                    assessmentGraph(navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthRedirect(intent)
    }

    private fun handleAuthRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            val scheme = uri.scheme
            val host = uri.host
            val path = uri.path

            if (scheme == "io.dev.pace" && host == "oauth2redirect") {
                val code = uri.getQueryParameter("code")
                val error = uri.getQueryParameter("error")

                when (path) {
                    "/twitter" -> loginViewModel.handleTwitterRedirect(code, error)
                    "/instagram" -> loginViewModel.handleInstagramRedirect(code, error)
                }
            }
        }
    }
}
