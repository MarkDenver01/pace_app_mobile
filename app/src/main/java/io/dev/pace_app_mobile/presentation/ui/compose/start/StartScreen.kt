package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.utils.SweetAlertDialog
import io.dev.pace_app_mobile.presentation.utils.sharedViewModel

@Composable
fun GlassEffectOverlay(
    modifier: Modifier = Modifier,
    blur: Float = 20f,     // mas mataas = mas frosted
    transparency: Float = 0.25f,
    cornerRadius: Float = 28f
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .blur(blur.dp) // 🔥 main blur effect
            .background(Color.White.copy(alpha = transparency)) // frosted glass tint
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.45f),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
    )
}

/* ============================================================
    MAIN START SCREEN (PREMIUM)
============================================================ */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController,
    universityId: String? = null,
    dynamicToken: String? = null
) {
    val scroll = rememberScrollState()
    val startViewModel: StartViewModel = sharedViewModel(navController)

    var showGuestDialog by remember { mutableStateOf(false) }
    var showOldNewDialog by remember { mutableStateOf(false) }

    /* ============================================================
       ELEGANT STATIC PREMIUM GRADIENT BACKGROUND
    ============================================================ */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFCC6A00), // darker premium orange
                        Color(0xFFFF8A00), // your current mid-tone
                        Color(0xFFFFC67A)  // highlight
                    )
                )
            )
            .verticalScroll(scroll)
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            /* ============================================================
               HEADER (SIMPLE, CLEAN, PREMIUM)
            ============================================================ */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                        spotColor = Color.Black.copy(alpha = 0.25f)
                    )
                    .paint(
                        painterResource(id = R.drawable.hero_bg),
                        contentScale = ContentScale.Crop
                    )
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .padding(bottom = 24.dp)
            ) {
                GlassEffectOverlay(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(580.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(30.dp))

                    Image(
                        painter = painterResource(id = R.drawable.pace_logo_full),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Discover Your Future",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E2604)
                    )

                    Text(
                        "One Click at a Time",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4E2604)
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        "Personalized course and career exploration designed\n" +
                                "to guide students toward the right academic path.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color(0xFF4E2604),
                        modifier = Modifier.padding(horizontal = 28.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.pace_hero_student),
                        contentDescription = "",
                        modifier = Modifier
                            .size(175.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .shadow(10.dp, RoundedCornerShape(18.dp))
                    )

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = { showGuestDialog = true },
                        modifier = Modifier
                            .height(50.dp)
                            .width(210.dp),
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD62828)
                        )
                    ) {
                        Text("Get Started", color = Color.White, fontSize = 17.sp)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))


            /* ============================================================
               PLATFORM FEATURES — CLEAN, ELEGANT
            ============================================================ */
            Text(
                "Platform Features",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                "Everything a student needs to identify strengths\n" +
                        "and plan a career path.",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            FeatureCard(
                icon = R.drawable.ic_feature_assessment,
                title = "Smart Career Assessment",
                desc = "Personality & Interest matching"
            )

            FeatureCard(
                icon = R.drawable.ic_feature_recommendations,
                title = "Course Recommendations",
                desc = "Top 3 personalized course suggestions"
            )

            FeatureCard(
                icon = R.drawable.ic_feature_links,
                title = "Institution Portal Links",
                desc = "Connect directly to partnered schools"
            )

            Spacer(Modifier.height(16.dp))


            /* ============================================================
               ABOUT + MISSION — CLEAN + SIMPLE
            ============================================================ */
            SectionBlock(
                title = "About Pace",
                content =
                    "PACE helps students discover their ideal academic paths while giving institutions tools to evaluate, guide, and track progress. " +
                            "It uses data insights to connect potential to opportunity."
            )

            SectionBlock(
                title = "Our Mission",
                content =
                    "To empower every learner to discover their path and every institution to guide them with purpose. We make academic and career exploration meaningful and data-driven."
            )

            Spacer(Modifier.height(28.dp))

            Text(
                "Your Choice, Your Future",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A3A00)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "© 2025 PACE System. All rights reserved.",
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }
    }

    /* ============================================================
       DIALOGS (UNCHANGED)
    ============================================================ */

    if (showGuestDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Take Assessment",
            message = "Do you want to continue as Guest?",
            show = showGuestDialog,
            onConfirm = {
                showGuestDialog = false
                startViewModel.setUserType(UserType.GUEST)
                navController.navigate(Routes.START_ASSESSMENT_ROUTE)
            },
            onDismiss = {
                showGuestDialog = false
                showOldNewDialog = true
            }
        )
    }

    if (showOldNewDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Student Registration",
            message = "Are you an old student or new student?",
            confirmText = "Old Student",
            dismissText = "New Student",
            show = showOldNewDialog,
            onConfirm = {
                showOldNewDialog = false
                startViewModel.setUserType(UserType.NEW)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/true")
            },
            onDismiss = {
                showOldNewDialog = false
                startViewModel.setUserType(UserType.OLD)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/false")
            }
        )
    }


    /* ============================================================
        DIALOG LOGIC
    ============================================================ */

    if (showGuestDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Take Assessment",
            message = "Do you want to continue as Guest?",
            show = showGuestDialog,
            onConfirm = {
                showGuestDialog = false
                startViewModel.setUserType(UserType.GUEST)
                navController.navigate(Routes.START_ASSESSMENT_ROUTE)
            },
            onDismiss = {
                showGuestDialog = false
                showOldNewDialog = true
            }
        )
    }

    if (showOldNewDialog) {
        SweetAlertDialog(
            type = AlertType.QUESTION,
            title = "Student Registration",
            message = "Are you an old student or new student?",
            confirmText = "Old Student",
            dismissText = "New Student",
            show = showOldNewDialog,
            onConfirm = {
                showOldNewDialog = false
                startViewModel.setUserType(UserType.OLD)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/true")
            },
            onDismiss = {
                showOldNewDialog = false
                startViewModel.setUserType(UserType.NEW)
                navController.navigate("${Routes.SIGN_UP_ROUTE}/false")
            }
        )
    }
}


/* ============================================================
    UNIVERSAL FADE + SLIDE EFFECT
============================================================ */
@Composable
fun AnimatedSection(content: @Composable ColumnScope.() -> Unit) {

    val enterAnim = fadeIn(tween(600)) +
            slideInVertically(
                initialOffsetY = { 80 },
                animationSpec = tween(600)
            )

    AnimatedVisibility(
        visible = true,
        enter = enterAnim
    ) {
        Column(content = content)
    }
}
