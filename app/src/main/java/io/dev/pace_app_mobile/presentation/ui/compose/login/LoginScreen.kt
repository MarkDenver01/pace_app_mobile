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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.AlertConfirmationDialog
import io.dev.pace_app_mobile.presentation.utils.AlertDynamicConfirmationDialog
import io.dev.pace_app_mobile.presentation.utils.CustomCheckBox
import io.dev.pace_app_mobile.presentation.utils.CustomDropDownPicker
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.CustomIconButton
import io.dev.pace_app_mobile.presentation.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var mailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRememberMe by remember { mutableStateOf(false) }

    val showDialog by viewModel.showDialog.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val navigateTo by viewModel.navigateTo.collectAsState()

    var googleIdToken by remember { mutableStateOf<String?>(null) }

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
                    viewModel.showUniversitySelector()
                } else {
                    Log.e("GoogleLogin", "ID Token is null")
                    viewModel.onErrorShown()
                }
            } catch (e: Exception) {
                Log.e("GoogleLogin", "Credential retrieval failed", e)
                viewModel.onErrorShown()
            }
        }
    }

    // observe navigation state
    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.onNavigationHandled()
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
                .padding(horizontal = 24.dp) // apply where needed
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
                        onCheckedChange = {
                            isRememberMe = it
                            viewModel.setRemember(it)
                        },
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
                        onClick = {
                            viewModel.fetchUniversities()
                            startGoogleLogin(oneTapClient, launcher, context)
                        }
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_facebook,
                        onClick = { viewModel.onAuthFacebookClick()}
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_twitter,
                        onClick = { viewModel.onAuthTwitterClick()}
                    )
                    CustomIconButton(
                        icon = R.drawable.ic_instagram,
                        onClick = { viewModel.onAuthInstagramClick()}
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDynamicConfirmationDialog(
            message = "Login successful!",
            alertType = AlertType.SUCCESS,
            onClose = {
                viewModel.onDialogDismissed()
            }
        )
    }

    // Error Dialog
    errorMessage?.let { error ->
        AlertDynamicConfirmationDialog(
            message = error,
            alertType = AlertType.ERROR,
            onClose = {
                viewModel.onErrorShown()
            }
        )
    }

    if (viewModel.showUniversityDialog.collectAsState().value) {
        AlertDialog(
            onDismissRequest = { viewModel.confirmUniversitySelection() },
            title = { Text("Select University") },
            text = {
                var selectedName by remember { mutableStateOf("") }
                val universities by viewModel.universities.collectAsState()

                CustomDropDownPicker(
                    selectedOption = selectedName,
                    onOptionSelected = { option ->
                        selectedName = option
                        val uniId = universities.find { it.universityName == option }?.universityId
                        uniId?.let { viewModel.setSelectedUniversity(it) }
                    },
                    options = universities.map { it.universityName },
                    placeholder = "Choose your university",
                    leadingIcon = Icons.Default.Edit
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.confirmUniversitySelection()
                        // Pass the stored token and the selected university ID
                        val universityId = viewModel.selectedUniversityId.value
                        if (googleIdToken != null && universityId != null) {
                            viewModel.onAuthGoogleClick(googleIdToken!!)
                        } else {
                            viewModel.onErrorShown() // Show an error if data is missing
                        }
                    },
                    enabled = viewModel.selectedUniversityId.collectAsState().value != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.confirmUniversitySelection() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun startGoogleLogin(
    oneTapClient: SignInClient,
    launcher: ActivityResultLauncher<IntentSenderRequest>,
    context: Context
) {
    val request = GetSignInIntentRequest.builder()
        // Use your Web Client ID here
        .setServerClientId("13172730276-np8mp31qu0gun6of0qch2gvder08hlaq.apps.googleusercontent.com")
        .build()

    oneTapClient.getSignInIntent(request)
        .addOnSuccessListener { pendingIntent ->
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            launcher.launch(intentSenderRequest)
        }
        .addOnFailureListener { e ->
            Log.e("GoogleLogin", "getSignInIntent failed", e)
        }
}