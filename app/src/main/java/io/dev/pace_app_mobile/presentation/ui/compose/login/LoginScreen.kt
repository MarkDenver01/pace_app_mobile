package io.dev.pace_app_mobile.presentation.ui.compose.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.navigation.Routes.START_ASSESSMENT_ROUTE
import io.dev.pace_app_mobile.navigation.Routes.START_ROUTE
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.AlertDynamicConfirmationDialog
import io.dev.pace_app_mobile.presentation.utils.CustomCheckBox
import io.dev.pace_app_mobile.presentation.utils.CustomDropDownPicker
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.CustomIconButton
import io.dev.pace_app_mobile.presentation.utils.CustomTextField
import io.dev.pace_app_mobile.presentation.utils.ProgressDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // UI state
    var mailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRememberMe by remember { mutableStateOf(false) }

    // Dialog and navigation states
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccessDialog by remember { mutableStateOf(false) }
    var isWarningDialog by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }
    var showUniversityDialog by remember { mutableStateOf(false) }
    var universityList by remember { mutableStateOf<List<UniversityResponse>>(emptyList()) }
    var googleIdToken by remember { mutableStateOf<String?>(null) }
    var selectedUniversity by remember { mutableStateOf("") }

    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }

    // Google sign-in launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                googleIdToken = credential.googleIdToken
                if (googleIdToken != null) {
                    viewModel.checkGoogleAccount(credential.id, googleIdToken!!)
                } else {
                    Log.e("GoogleLogin", "ID Token is null")
                    showDialog = true
                    dialogTitle = "Error"
                    dialogMessage = "Failed to retrieve Google ID Token."
                    isSuccessDialog = false
                    isWarningDialog = false
                }
            } catch (e: Exception) {
                Log.e("GoogleLogin", "Credential retrieval failed", e)
                showDialog = true
                dialogTitle = "Error"
                dialogMessage = "Credential retrieval failed. Please try again."
                isSuccessDialog = false
                isWarningDialog = false
            }
        }
    }

    // Collect one-time events from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is LoginEvent.ShowProgressDialog -> showProgressDialog = event.isVisible
                is LoginEvent.ShowSuccessDialog -> {
                    dialogTitle = "Success"
                    dialogMessage = event.message
                    isWarningDialog = false
                    isSuccessDialog = true
                    showDialog = true
                }

                is LoginEvent.ShowWarningDialog -> {
                    dialogTitle = "Warning"
                    dialogMessage = event.message
                    isWarningDialog = true
                    isSuccessDialog = false
                    showDialog = true
                }

                is LoginEvent.ShowErrorDialog -> {
                    dialogTitle = "Error"
                    dialogMessage = event.message
                    isSuccessDialog = false
                    isWarningDialog = false
                    showDialog = true
                }

                is LoginEvent.NavigateTo -> navController.navigate(event.route)
                is LoginEvent.ShowUniversityDialog -> {
                    universityList = event.universities
                    googleIdToken = event.googleIdToken
                    showUniversityDialog = true
                }
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
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(spacing.lg))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.login_title),
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCC4A1A),
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                CustomTextField(
                    value = mailAddress,
                    onValueChange = { mailAddress = it },
                    placeholder = "Email",
                    leadingIcon = Icons.Default.Email,
                    fontSize = sizes.buttonFontSize,
                )

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isPassword = true,
                    leadingIcon = Icons.Default.Lock,
                    fontSize = sizes.buttonFontSize
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomCheckBox(
                        checked = isRememberMe,
                        onCheckedChange = { isRememberMe = it },
                        label = "Remember me"
                    )
                    Text(
                        text = "Forgot password?",
                        color = Color(0xFFCC4A1A),
                        modifier = Modifier.clickable {
                            // TODO: navigate to forgot password screen
                        }
                    )
                }

                Spacer(modifier = Modifier.height(spacing.sm))

                CustomDynamicButton(
                    onClick = { viewModel.onLoginClick(mailAddress, password) },
                    content = stringResource(id = R.string.button_login)
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Text(
                    text = buildAnnotatedString {
                        append("Don't you have an account? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Create here.")
                        }
                    },
                    color = Color(0xFFCC4A1A),
                    modifier = Modifier.clickable {
                        viewModel.onSignupClick()
                    }
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Text(
                    text = "Or Sign in with ",
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFCC4A1A),
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CustomIconButton(
                        icon = R.drawable.ic_google,
                        onClick = { startGoogleLogin(oneTapClient, launcher) }
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_facebook,
                        onClick = { viewModel.onAuthFacebookClick() }
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_twitter,
                        onClick = { viewModel.onAuthTwitterClick() }
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_instagram,
                        onClick = { viewModel.onAuthInstagramClick() }
                    )
                }
            }
        }
    }

    // Displays a progress dialog when the state is loading
    if (showProgressDialog) {
        ProgressDialog()
    }

    // Displays a dynamic alert dialog based on the event
    if (showDialog) {
        AlertDynamicConfirmationDialog(
            message = dialogMessage,
            alertType = when {
                isWarningDialog -> AlertType.WARNING
                isSuccessDialog -> AlertType.SUCCESS
                else -> AlertType.ERROR
            },
            onClose = {
                showDialog = false
                if (isSuccessDialog) {
                    navController.navigate(START_ASSESSMENT_ROUTE)
                }
            }
        )
    }

    // University Selection Dialog
    if (showUniversityDialog) {
        AlertDialog(
            onDismissRequest = { showUniversityDialog = false },
            title = { Text("Select University") },
            text = {
                CustomDropDownPicker(
                    selectedOption = selectedUniversity,
                    onOptionSelected = { option ->
                        selectedUniversity = option
                        val uniId =
                            universityList.find { it.universityName == option }?.universityId
                        uniId?.let { viewModel.setSelectedUniversity(it) }
                        uniId?.let { viewModel.setSelectedUniversity(it) }
                    },
                    options = universityList.map { it.universityName },
                    placeholder = "Choose your university",
                    leadingIcon = Icons.Default.Edit
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showUniversityDialog = false
                        viewModel.onAuthGoogleClick(googleIdToken!!, false)
                    },
                    enabled = viewModel.selectedUniversityId.collectAsState().value != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showUniversityDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun startGoogleLogin(
    oneTapClient: SignInClient,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val request = GetSignInIntentRequest.builder()
        // Use your Web Client ID here
        .setServerClientId("13172730276-np8mp31qu0gun6of0qch2gvder08hlaq.apps.googleusercontent.com")
        .build()

    oneTapClient.getSignInIntent(request)
        .addOnSuccessListener { pendingIntent ->
            val intentSenderRequest =
                IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            launcher.launch(intentSenderRequest)
        }
        .addOnFailureListener { e ->
            Log.e("GoogleLogin", "getSignInIntent failed", e)
        }
}