package io.dev.pace_app_mobile.presentation.ui.compose.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.dynamic_links.DynamicLinkViewModel
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.AlertDynamicConfirmationDialog
import io.dev.pace_app_mobile.presentation.utils.CustomCheckBox
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    isOldStudent: Boolean = false,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    dynamicLinkViewModel: DynamicLinkViewModel = hiltViewModel()
) {
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val colors = LocalAppColors.current

    var mailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val navigateTo by signUpViewModel.navigateTo.collectAsState()
    val showAgreeDialog by signUpViewModel.showAgreeWarningDialog.collectAsState()
    val showSuccessDialog by signUpViewModel.showSuccessDialog.collectAsState()
    val showErrorDialog by signUpViewModel.showErrorDialog.collectAsState()
    val errorMessage by signUpViewModel.errorMessage.collectAsState()
    val university by signUpViewModel.university.collectAsState()
    val storedLink by dynamicLinkViewModel.dynamicLink.collectAsState(initial = null)
    val universityDomainEmail by signUpViewModel.universityDomainEmail.collectAsState()

    var agree by remember { mutableStateOf(false) }


    // Fetch the specific university once when universityId is provided
    LaunchedEffect(storedLink?.universityId) {
        storedLink?.universityId?.let { id ->
            signUpViewModel.fetchUniversityById(id)
            signUpViewModel.fetchUniversityDomainEmail(id)
        }
    }

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            signUpViewModel.resetNavigation()
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
                    text = stringResource(id = R.string.signup_title),
                    fontSize = sizes.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = spacing.md)
                )

                CustomTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "First Name",
                    leadingIcon = Icons.Default.AccountCircle,
                    fontSize = sizes.buttonFontSize,
                )

                CustomTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Last Name",
                    leadingIcon = Icons.Default.AccountCircle,
                    fontSize = sizes.buttonFontSize,
                )

                // University field (read-only)
                CustomTextField(
                    value = university?.universityName ?: "",
                    onValueChange = {},
                    placeholder = "University",
                    leadingIcon = Icons.Default.Edit,
                    fontSize = sizes.smallFontSize,
                    enabled = false
                )

                val domain = universityDomainEmail?.domainEmail ?: ""
                val emailValue = if (isOldStudent && domain.isNotEmpty()) {
                    mailAddress + domain
                } else mailAddress

                CustomTextField(
                    value = emailValue,
                    onValueChange = { input ->
                        mailAddress = if (isOldStudent && domain.isNotEmpty()) {
                            input.removeSuffix(domain)
                        } else input
                    },
                    placeholder = "University Domain Email",
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

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Confirm Password",
                    isPassword = true,
                    leadingIcon = Icons.Default.Lock,
                    fontSize = sizes.buttonFontSize
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                CustomCheckBox(
                    checked = agree,
                    onCheckedChange = { agree = it },
                    annotatedLabel = buildAnnotatedString {
                        append("I agree with ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("privacy")
                        }
                        append(" and ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("policy")
                        }
                    }
                )

                CustomDynamicButton(
                    onClick = {
                        val selectedUniversityId = storedLink?.universityId
                        if (selectedUniversityId != null) {
                            signUpViewModel.onSignupClick(
                                agreed = agree,
                                username = "$firstName $lastName",
                                email = if (isOldStudent) mailAddress + domain else mailAddress,
                                password = password,
                                universityId = selectedUniversityId,
                                token = storedLink!!.dynamicToken,
                                onSuccess = {
                                    signUpViewModel.showSuccessDialog()
                                }
                            )
                        } else {
                            signUpViewModel.showErrorDialog("University not found.")
                        }
                    },
                    content = stringResource(id = R.string.button_sign_up),
                    backgroundColor = colors.primary,
                    pressedBackgroundColor = colors.pressed
                )
            }
        }
    }

    if (showAgreeDialog) {
        AlertDynamicConfirmationDialog(
            message = "You must agree to the terms.",
            alertType = AlertType.WARNING,
            onClose = { signUpViewModel.dismissDialogs() }
        )
    }

    if (showSuccessDialog) {
        AlertDynamicConfirmationDialog(
            message = "Register successful!",
            alertType = AlertType.SUCCESS,
            onClose = {
                signUpViewModel.dismissDialogs()
                signUpViewModel.onSuccessTransition()
            }
        )
    }

    if (showErrorDialog && errorMessage != null) {
        AlertDynamicConfirmationDialog(
            message = errorMessage!!,
            alertType = AlertType.ERROR,
            onClose = { signUpViewModel.dismissDialogs() }
        )
    }
}
