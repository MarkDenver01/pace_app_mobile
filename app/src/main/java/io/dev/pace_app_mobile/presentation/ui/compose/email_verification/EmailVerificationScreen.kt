package io.dev.pace_app_mobile.presentation.ui.compose.email_verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    navController: NavController,
    email: String,
    emailVerificationViewModel: EmailVerificationViewModel = hiltViewModel(),
    ) {
    val uiState by emailVerificationViewModel.uiState.collectAsState()
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current

    val digitCount = 4
    val digits = remember { List(digitCount) { mutableStateOf("") } }
    val focusRequesters = remember { List(digitCount) { FocusRequester() } }

    val combinedCode by remember { derivedStateOf { digits.joinToString("") { it.value } } }

    // Start countdown when user completes 4 digits
    LaunchedEffect(combinedCode) {
        if (combinedCode.length == 4 && uiState.countdown == 0) {
            emailVerificationViewModel.onCodeComplete()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgApp),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "",
                showLeftButton = false,
                showRightButton = false,
                onLeftClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(spacing.xl))

            Text(
                text = "Verify your email",
                fontWeight = FontWeight.Bold,
                fontSize = sizes.titleFontSize,
                color = colors.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.sm))

            Text(
                text = buildAnnotatedString {
                    append("Enter the 4-digit code sent to ")
                    withStyle(SpanStyle(color = colors.primary, fontWeight = FontWeight.SemiBold)) {
                        append(email)
                    }
                },
                color = colors.primary,
                fontSize = sizes.smallFontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            // OTP Fields
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 0 until digitCount) {
                    OutlinedTextField(
                        value = digits[i].value,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isDigit() }.take(1)
                            digits[i].value = filtered
                            if (filtered.isNotEmpty() && i < digitCount - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .focusRequester(focusRequesters[i]),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = sizes.titleFontSize,
                            color = colors.primary
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.pressed,
                            cursorColor = colors.primary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (i == digitCount - 1) ImeAction.Done else ImeAction.Next
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.lg))

            // Countdown / Resend
            if (uiState.countdown > 0) {
                Text(
                    text = "Resend available in ${uiState.countdown}s",
                    color = Color.Gray
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { emailVerificationViewModel.resendCode(email) }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.primary)
                    Spacer(Modifier.width(6.dp))
                    Text("Resend Code", color = colors.primary, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(spacing.lg))

            // Verify Button
            CustomDynamicButton(
                onClick = { emailVerificationViewModel.verifyCode(email, combinedCode) },
                 content = "VERIFY NOW",
                backgroundColor = colors.primary,
                pressedBackgroundColor = colors.pressed,
                enabled = uiState.isVerifyEnabled
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            Text(
                text = "Back to sign up",
                color = colors.primary,
                fontSize = sizes.buttonFontSize,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
    }

    // Success dialog
    if (uiState.verifySuccess) {
        SweetAlertDialog(
            type = AlertType.SUCCESS,
            title = "Account Verification Success",
            message = "Successfully verified. You may now proceed on logging in.",
            show = true,
            onConfirm = {
                emailVerificationViewModel.reset()
                navController.navigate("login_route") {
                    popUpTo("email_verification_route") { inclusive = true }
                }
            },
            confirmText = "Complete",
            isSingleButton = true
        )
    }
}
