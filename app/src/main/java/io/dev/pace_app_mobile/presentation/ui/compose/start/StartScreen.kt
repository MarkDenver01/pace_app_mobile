package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.ui.compose.dynamic_links.DynamicLinkViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import io.dev.pace_app_mobile.presentation.utils.UniversityLogo
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController,
    universityId: String? = null,
    dynamicToken: String? = null
) {
    val startViewModel: StartViewModel = sharedViewModel(navController)
    val dynamicLinkViewModel: DynamicLinkViewModel = sharedViewModel(navController)
    val colors = LocalAppColors.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showGuestDialog by remember { mutableStateOf(false) }
    var showOldNewStudentDialog by remember { mutableStateOf(false) }
    val logoPath by startViewModel.logoPath.collectAsState()
    val storedLink by dynamicLinkViewModel.dynamicLink.collectAsState(initial = null)


    // 🔹 Automatically show the dialog if opened from a dynamic link
    LaunchedEffect(Unit) {
        if (!universityId.isNullOrEmpty() && !dynamicToken.isNullOrEmpty()) {
            startViewModel.fetchLogoPathWithDynamicUrl(universityId.toLong())
        } else {
            startViewModel.fetchLogoPathWithDynamicUrl(
                storedLink?.universityId ?: 0L
            )
        }
    }

    // 🔹 Navigation observer
    LaunchedEffect(Unit) {
        startViewModel.navigateTo.collect {
            navController.navigate(Routes.TITLE_ROUTE) {
                popUpTo(Routes.START_ROUTE) { inclusive = true }
            }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Logo Block ---

            UniversityLogo(logoPath)

            // --- Get Started Button ---
            CustomDynamicButton(
                onClick = { showGuestDialog = true },
                content = stringResource(id = R.string.button_get_started),
                backgroundColor = colors.primary,
                pressedBackgroundColor = colors.pressed
            )
        }
    }

    // 🔹 Guest Dialog
    if (showGuestDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Take Assessment",
            message = "Do you want to take the assessment as a Guest? You can continue without signing up.",
            show = showGuestDialog,
            onConfirm = {
                startViewModel.setUserType(UserType.GUEST)
                showGuestDialog = false
                navController.navigate(Routes.START_ASSESSMENT_ROUTE) {
                    popUpTo(Routes.START_ROUTE) { inclusive = false }
                }
            },
            onDismiss = {
                showGuestDialog = false
                showOldNewStudentDialog = true
            }
        )
    }

    if (showOldNewStudentDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Student Registration",
            message = "Are you an old student or new student?",
            show = showOldNewStudentDialog,
            onConfirm = {
                showOldNewStudentDialog = false
                startViewModel.setUserType(UserType.OLD)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/false")
            },
            onDismiss = {
                showOldNewStudentDialog = false
                startViewModel.setUserType(UserType.NEW)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/true")
            },
            "New Student",
            "Old Student"
        )
    }
}
