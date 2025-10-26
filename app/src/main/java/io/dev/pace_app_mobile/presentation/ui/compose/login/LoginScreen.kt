package io.dev.pace_app_mobile.presentation.ui.compose.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultRegistryOwner
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
import androidx.compose.runtime.DisposableEffect
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult as FacebookLoginResult
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.navigation.Routes.START_ASSESSMENT_ROUTE
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomCheckBox
import io.dev.pace_app_mobile.presentation.utils.CustomDropDownPicker
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.CustomIconButton
import io.dev.pace_app_mobile.presentation.utils.CustomTextField
import io.dev.pace_app_mobile.presentation.utils.OAuthProviders
import io.dev.pace_app_mobile.presentation.utils.OAuthProviders.IG_AUTH_URI
import io.dev.pace_app_mobile.presentation.utils.ProgressDialog
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.CodeVerifierUtil
import net.openid.appauth.ResponseTypeValues
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    universityId: String? = null,
    dynamicToken: String,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = LocalAppColors.current

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
    var facebookAccessToken by remember { mutableStateOf<String?>(null) }
    var selectedUniversity by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as Activity
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val authService = remember { AuthorizationService(context) }
    val callbackManager = remember { CallbackManager.Factory.create() }
    var twitterAccessToken by remember { mutableStateOf<String?>(null) }
    var instagramAuthCode by remember { mutableStateOf<String?>(null) }

    val instagramAuthLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data?.data != null) {
                val redirect =
                    result.data!!.data!! // io.dev.pace://oauth2redirect/instagram?code=...
                val code = redirect.getQueryParameter("code")
                if (code != null) {
                    // New flow: send CODE to backend (backend should exchange for access_token securely)
                    // If the backend returns BAD_REQUEST for missing university, we will open the picker
                    viewModel.onAuthInstagramClick(code, exists = true)
                } else {
                    Timber.e("No authorization code in redirect")
                }
            }
        }

    val twitterAuthLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val resp = AuthorizationResponse.fromIntent(result.data!!)
                val ex = AuthorizationException.fromIntent(result.data!!)
                if (resp != null) {
                    // Exchange code for tokens (handled by AppAuth; no client secret when using PKCE)
                    val tokenReq = resp.createTokenExchangeRequest()
                    authService.performTokenRequest(tokenReq) { tokenResp, tokenEx ->
                        if (tokenResp != null) {
                            val accessToken = tokenResp.accessToken
                            if (accessToken != null) {
                                // Try backend login immediately; if backend needs university, trigger viewModel.startTwitterNewUser()
                                viewModel.onAuthTwitterClick(accessToken, exists = true)
                            }
                        } else {
                            Timber.e("Token exchange failed: ${tokenEx?.errorDescription}")
                        }
                    }
                } else {
                    Timber.e("Authorization error: ${ex?.errorDescription}")
                }
            }
        }

    DisposableEffect(Unit) {
        val loginManager = LoginManager.getInstance()
        val callback = object : FacebookCallback<FacebookLoginResult> {
            override fun onSuccess(result: FacebookLoginResult) {
                val token = result.accessToken.token
                if (token != null) {
                    // Try login immediately; if backend needs university, VM will emit dialog event
                    viewModel.checkFacebookAccount(token)
                    // keep token for potential follow-up new-user flow
                    facebookAccessToken = token
                } else {
                    showDialog = true
                    dialogTitle = "Error"
                    dialogMessage = "Failed to retrieve Facebook access token."
                    isSuccessDialog = false
                    isWarningDialog = false
                }
            }

            override fun onCancel() {
                showDialog = true
                dialogTitle = "Canceled"
                dialogMessage = "Facebook login canceled."
                isSuccessDialog = false
                isWarningDialog = true
            }

            override fun onError(error: FacebookException) {
                showDialog = true
                dialogTitle = "Error"
                dialogMessage = error.localizedMessage ?: "Facebook login failed."
                isSuccessDialog = false
                isWarningDialog = false
            }
        }

        loginManager.registerCallback(callbackManager, callback)
        onDispose { loginManager.unregisterCallback(callbackManager) }
    }

    // --- Google sign-in launcher ---
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
                    // Google flow
                    universityList = event.universities
                    googleIdToken = event.googleIdToken
                    facebookAccessToken = null; instagramAuthCode = null; twitterAccessToken = null
                    showUniversityDialog = true
                }

                is LoginEvent.ShowUniversityDialogFacebook -> {
                    // Facebook flow
                    universityList = event.universities
                    facebookAccessToken = event.facebookAccessToken
                    googleIdToken = null; twitterAccessToken = null; instagramAuthCode = null
                    showUniversityDialog = true
                }

                is LoginEvent.ShowUniversityDialogTwitter -> {
                    universityList = event.universities
                    twitterAccessToken = event.twitterAccessToken
                    googleIdToken = null; facebookAccessToken = null; instagramAuthCode = null
                    showUniversityDialog = true
                }

                is LoginEvent.ShowUniversityDialogInstagram -> {
                    universityList = event.universities
                    instagramAuthCode = event.authorizationCode
                    googleIdToken = null; facebookAccessToken = null; twitterAccessToken = null
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
                    color = colors.primary,
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
                        color = colors.primary,
                        modifier = Modifier.clickable {
                            // TODO: navigate to forgot password screen
                        }
                    )
                }

                Spacer(modifier = Modifier.height(spacing.sm))

                CustomDynamicButton(
                    onClick = {
                        viewModel.onLoginClick(mailAddress, password)
                    },
                    content = stringResource(id = R.string.button_login),
                    backgroundColor = colors.primary,
                    pressedBackgroundColor = colors.pressed
                )

                Spacer(modifier = Modifier.height(spacing.md))

//                Text(
//                    text = buildAnnotatedString {
//                        append("Don't you have an account? ")
//                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
//                            append("Create here.")
//                        }
//                    },
//                    color = colors.primary,
//                    modifier = Modifier.clickable {
//                        viewModel.onSignupClick()
//                    }
//                )

//                Spacer(modifier = Modifier.height(spacing.md))

                Text(
                    text = "Or Sign in with ",
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CustomIconButton(
                        icon = R.drawable.ic_google,
                        onClick = { startGoogleLogin(oneTapClient, launcher) },
                        text = "Sign in with Google",
                        fillMaxWidth = true,
                        backgroundColor = colors.primary,
                        pressedBackgroundColor = colors.pressed,
                        contentColor = colors.primary,
                        borderColor = colors.primary,
                        cornerRadius = 24.dp
                    )
//                    CustomIconButton(
//                        icon = R.drawable.ic_facebook,
//                        onClick = {
//                            val registryOwner = activity as ActivityResultRegistryOwner
//                            LoginManager.getInstance().logIn(
//                                registryOwner,
//                                callbackManager,
//                                listOf("email", "public_profile")
//                            )
//                        },
//                        backgroundColor = colors.primary,
//                        pressedBackgroundColor = colors.pressed,
//                        contentColor = colors.primary,
//                        borderColor = colors.primary
//                    )
//                    CustomIconButton(
//                        icon = R.drawable.ic_twitter,
//                        onClick = { startTwitterLogin(authService, twitterAuthLauncher) },
//                        backgroundColor = colors.primary,
//                        pressedBackgroundColor = colors.pressed,
//                        contentColor = colors.primary,
//                        borderColor = colors.primary
//                    )
//                    CustomIconButton(
//                        icon = R.drawable.ic_instagram,
//                        onClick = {
//                            // Instagram Basic Display authorization (response_type=code)
//                            val authUri = Uri.parse(IG_AUTH_URI).buildUpon()
//                                .appendQueryParameter("client_id", OAuthProviders.IG_CLIENT_ID)
//                                .appendQueryParameter(
//                                    "redirect_uri",
//                                    OAuthProviders.IG_REDIRECT_URI
//                                )
//                                .appendQueryParameter(
//                                    "scope",
//                                    "user_profile"
//                                ) // 'user_media' optional
//                                .appendQueryParameter("response_type", "code")
//                                .build()
//
//
//                            val intent = Intent(Intent.ACTION_VIEW, authUri)
//                            instagramAuthLauncher.launch(intent)
//                        },
//                        backgroundColor = colors.primary,
//                        pressedBackgroundColor = colors.pressed,
//                        contentColor = colors.primary,
//                        borderColor = colors.primary
//                    )
                }
            }
        }
    }

    if (showProgressDialog) {
        ProgressDialog()
    }

    if (showDialog) {
        SweetAlertDialog(
            type = when {
                isWarningDialog -> AlertType.WARNING
                isSuccessDialog -> AlertType.SUCCESS
                else -> AlertType.ERROR
            },
            title = when {
                isWarningDialog -> "Warning"
                isSuccessDialog -> "Success"
                else -> "Error"
            },
            message = dialogMessage,
            show = true,
            onConfirm = {
                showDialog = false
                if (isSuccessDialog) {
                    navController.navigate(START_ASSESSMENT_ROUTE)
                }
            },
            confirmText = "Close",
            isSingleButton = true
        )
    }

    // University Selection Dialog (reused for Google & Facebook)
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
                        when {
                            googleIdToken != null -> {
                                viewModel.onAuthGoogleClick(googleIdToken!!, exists = false)
                            }

                            facebookAccessToken != null -> {
                                viewModel.onAuthFacebookClick(facebookAccessToken!!, exists = false)
                            }

                            twitterAccessToken != null -> {
                                viewModel.onAuthTwitterClick(twitterAccessToken!!, exists = false)
                            }

                            instagramAuthCode != null -> {
                                viewModel.onAuthInstagramClick(instagramAuthCode!!, exists = false)
                            }
                        }
                    },
                    enabled = viewModel.selectedUniversityId.collectAsState().value != null
                ) { Text("OK") }
            },
            dismissButton = {
                Button(onClick = { showUniversityDialog = false }) { Text("Cancel") }
            }
        )
    }
}

private fun startGoogleLogin(
    oneTapClient: SignInClient,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val request = GetSignInIntentRequest.builder()
        .setServerClientId("13172730276-np8mp31qu0gun6of0qch2gvder08hlaq.apps.googleusercontent.com")
        .build()

    oneTapClient.getSignInIntent(request)
        .addOnSuccessListener { pendingIntent ->
            val intentSenderRequest =
                IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            launcher.launch(intentSenderRequest)
        }
        .addOnFailureListener { e ->
            Timber.e("getSignInIntent failed: ${e.message}")
        }
}

fun startTwitterLogin(
    authService: AuthorizationService,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val authRequest = AuthorizationRequest.Builder(
        AuthorizationServiceConfiguration(
            Uri.parse(OAuthProviders.TW_AUTH_URI),
            Uri.parse(OAuthProviders.TW_TOKEN_URI)
        ),
        OAuthProviders.TW_CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(OAuthProviders.TW_REDIRECT_URI)
    )
        .setScopes(*OAuthProviders.TW_SCOPES.toTypedArray())
        .setCodeVerifier(io.dev.pace_app_mobile.presentation.utils.CodeVerifierUtil.generateRandomCodeVerifier())
        .build()


    val intent = authService.getAuthorizationRequestIntent(authRequest)
    launcher.launch(intent)
}

