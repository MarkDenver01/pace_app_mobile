package io.dev.pace_app_mobile.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.Customization
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedUniversityLink
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.navigation.assessmentGraph
import io.dev.pace_app_mobile.navigation.startGraph
import io.dev.pace_app_mobile.navigation.titleGraph
import io.dev.pace_app_mobile.presentation.theme.Pace_app_mobileTheme
import io.dev.pace_app_mobile.presentation.ui.compose.CustomizationViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.dynamic_links.DynamicLinkViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.login.LoginViewModel
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import io.dev.pace_app_mobile.presentation.utils.UpdateInfo
import io.dev.pace_app_mobile.presentation.utils.checkForUpdate
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val customizationViewModel: CustomizationViewModel by viewModels()
    private val dynamicLinkViewModel: DynamicLinkViewModel by viewModels()
    private var isDynamicLinkTapped: Boolean = false
    private var universityId: String? = null
    private var dynamicToken: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Handle dynamic link & OAuth redirects
        handleDynamicLink(intent)
        handleAuthRedirect(intent)

        setContent {
            val theme by customizationViewModel.themeState.collectAsState()
           // val storedLink by dynamicLinkViewModel.dynamicLink.collectAsState(initial = null)
            val verifiedAccount by dynamicLinkViewModel.verifiedAccount.collectAsState(initial = null)

            var showUpdateDialog by remember { mutableStateOf(false) }
            var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }

            LaunchedEffect(Unit) {
                val info = checkForUpdate(this@MainActivity)
                if (info != null) {
                    updateInfo = info
                    showUpdateDialog = true
                }
            }

            Pace_app_mobileTheme(theme ?: Customization.lightTheme) {
                val navController = rememberNavController()

                // Decide startDestination
                val startDestination = if (isDynamicLinkTapped) {
                    // Priority 1: Dynamic link tapped
                    Routes.START_ROUTE
                } else {
                    if (verifiedAccount?.verified == true) {
                        // Priority 2: Verified account
                        Routes.LOGIN_ROUTE
                    } else if (loginViewModel.isUserLoggedIn()) {
                        // Priority 3: Logged in
                        Routes.START_ASSESSMENT_ROUTE
                    } else {
                        // Default
                        Routes.START_ROUTE
                    }
                }

                Timber.d("Navigation startDestination → $startDestination")

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    route = "root_graph"
                ) {
                    startGraph(navController, universityId, dynamicToken)
                    titleGraph(navController, universityId, dynamicToken)
                    assessmentGraph(navController)
                }

                if (showUpdateDialog && updateInfo != null) {
                    SweetAlertDialog(
                        type = AlertType.INFO,
                        title = "New Update Available (${updateInfo!!.latestVersionName})",
                        message = updateInfo!!.releaseNotes,
                        show = showUpdateDialog,
                        confirmText = "Update Now",
                        dismissText = "Later",
                        onConfirm = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo!!.apkUrl))
                            startActivity(intent)
                            showUpdateDialog = false
                        },
                        onDismiss = {
                            showUpdateDialog = false
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle dynamic link if activity is reused
        handleDynamicLink(intent)

        // Handle OAuth redirect
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

    /**
     * Extracts dynamic link params and applies customization theme
     */
    private fun handleDynamicLink(intent: Intent?) {
        intent?.data?.let { uri ->
            isDynamicLinkTapped = true
            universityId = uri.getQueryParameter("universityId")
            dynamicToken = uri.getQueryParameter("token").orEmpty()

            Timber.d("Dynamic link → universityId: $universityId, token: $dynamicToken")

            val uniId = universityId?.toLongOrNull() ?: 0L

            val data = SharedDynamicLink(
                universityId = uniId,
                dynamicToken = dynamicToken,
                isVerified = false
            )

            val base = uri.host
            val universityLink = SharedUniversityLink(base.toString())

            // Save to DataStore via ViewModel (Hilt DI)
            lifecycleScope.launch {
                dynamicLinkViewModel.saveLink(data)
                dynamicLinkViewModel.saveUniversityLink(universityLink)
            }

            Timber.d("Stored dynamic link (DataStore via ViewModel): $data")

            // Load theme dynamically
            customizationViewModel.loadTheme(uniId)
        }
    }
}
