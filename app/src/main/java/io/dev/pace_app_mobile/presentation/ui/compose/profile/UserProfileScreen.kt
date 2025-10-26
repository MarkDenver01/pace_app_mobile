package io.dev.pace_app_mobile.presentation.ui.compose.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.presentation.theme.BgApp
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import io.dev.pace_app_mobile.presentation.ui.compose.navigation.TopNavigationBar
import io.dev.pace_app_mobile.presentation.utils.CustomDynamicButton
import io.dev.pace_app_mobile.presentation.utils.SweetChangePasswordDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    userName: String = "John Doe",
    email: String = "johndoe@email.com",
    universityName: String = "Pace University",
    onUpdateProfile: (String, String) -> Unit = { _, _ -> },
    onChangePassword: (String, String) -> Unit = { _, _ -> }
) {
    val colors = LocalAppColors.current
    val spacing = LocalAppSpacing.current
    val sizes = LocalResponsiveSizes.current

    var name by remember { mutableStateOf(userName) }
    var mail by remember { mutableStateOf(email) }
    var university by remember { mutableStateOf(universityName) }

    var isEditing by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgApp),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "",
                showLeftButton = true,
                leftIcon = R.drawable.ic_back,
                showRightButton = false,
                onLeftClick = {
                    // TODO BACK
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .size(120.dp)
                    .background(color = colors.primary.copy(alpha = 0.05f), shape = CircleShape)
                    .border(2.dp, colors.primary.copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            Text(
                text = "Student Information",
                fontSize = sizes.titleFontSize,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.md))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                singleLine = true,
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colors.primary,
                    cursorColor = colors.primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.LightGray.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(spacing.sm))

            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Email") },
                singleLine = true,
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colors.primary,
                    cursorColor = colors.primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.LightGray.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(spacing.sm))

            OutlinedTextField(
                value = university,
                onValueChange = {},
                label = { Text("University") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colors.primary,
                    cursorColor = colors.primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.LightGray.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            // Edit or Update Profile Button
            CustomDynamicButton(
                onClick = {
                    if (isEditing) {
                        onUpdateProfile(name, mail)
                    }
                    isEditing = !isEditing
                },
                content = if (isEditing) "Update Profile" else "Edit Profile",
                backgroundColor = colors.primary,
                pressedBackgroundColor = colors.pressed
            )

            Spacer(modifier = Modifier.height(spacing.xl))

            Divider(color = colors.primary.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(spacing.lg))

            CustomDynamicButton(
                onClick = { showChangePasswordDialog = true },
                content = "Change Password",
                backgroundColor = Color.Gray.copy(alpha = 0.8f),
                pressedBackgroundColor = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(spacing.xxl))
        }
    }

    // 🔹 New Sweet Dialog for Password Change
    SweetChangePasswordDialog(
        show = showChangePasswordDialog,
        onConfirm = { newPass, confirmPass ->
            if (newPass == confirmPass) {
                onChangePassword("", newPass)
            }
            showChangePasswordDialog = false
        },
        onDismiss = { showChangePasswordDialog = false }
    )
}
