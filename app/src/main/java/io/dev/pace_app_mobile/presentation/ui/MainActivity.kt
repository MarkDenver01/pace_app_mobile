package io.dev.pace_app_mobile.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.navigation.assessmentGraph
import io.dev.pace_app_mobile.navigation.loginGraph
import io.dev.pace_app_mobile.navigation.signUpGraph
import io.dev.pace_app_mobile.navigation.startGraph
import io.dev.pace_app_mobile.navigation.titleGraph
import io.dev.pace_app_mobile.presentation.theme.Pace_app_mobileTheme
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Handle intent when activity is launched with redirect URI
        handleAuthRedirect(intent)

        setContent {
            Pace_app_mobileTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.START_ROUTE
                ) {
                    startGraph(navController)
                    titleGraph(navController)
                    loginGraph(navController)
                    signUpGraph(navController)
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
        val viewModel: LoginViewModel by viewModels()
        intent?.data?.let { uri ->
            val scheme = uri.scheme
            val host = uri.host
            val path = uri.path

            if (scheme == "io.dev.pace" && host == "oauth2redirect") {
                val code = uri.getQueryParameter("code")
                val error = uri.getQueryParameter("error")

                when (path) {
                    "/twitter" -> viewModel.handleTwitterRedirect(code, error)
                    "/instagram" -> viewModel.handleInstagramRedirect(code, error)
                }
            }
        }
    }
}