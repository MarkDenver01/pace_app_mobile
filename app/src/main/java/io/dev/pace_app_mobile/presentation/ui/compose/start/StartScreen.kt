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


/* ============================================================
    ULTRA-PREMIUM BACKGROUND (Animated + Breathing Gradient)
============================================================ */
@Composable
fun UltraPremiumGradientBackground(content: @Composable () -> Unit) {

    val infinite = rememberInfiniteTransition()

    val shift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            tween(9000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF8A00),   // premium orange
            Color(0xFFFFB45E),   // soft pastel orange
            Color(0xFFFFD8A8)    // luxury cream highlight
        ),
        start = Offset(0f, shift),
        end = Offset(shift, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        content()
    }
}


/* ============================================================
    APPLE-LIKE GLOW OVERLAY
============================================================ */
@Composable
fun UltraHighlightOverlay() {

    val infinite = rememberInfiniteTransition()

    val glowX by infinite.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            tween(8500, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.18f),
                    Color.Transparent
                ),
                center = Offset(glowX, size.height * 0.3f),
                radius = size.minDimension * 0.9f
            )
        )
    }
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

    val parallaxOffset = scroll.value * 0.22f

    UltraPremiumGradientBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {

            UltraHighlightOverlay()   // Apple-style glow

            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                /* ============================================================
                    HEADER (PARALLAX + PREMIUM)
                ============================================================ */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { translationY = -parallaxOffset / 2 }
                        .shadow(
                            elevation = 18.dp,
                            spotColor = Color.Black.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            )
                        )
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFC784),
                                    Color(0xFFFF9B27)
                                )
                            )
                        )
                        .padding(bottom = 22.dp)
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Image(
                            painter = painterResource(id = R.drawable.pace_header_pattern),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .alpha(0.4f),
                            contentScale = ContentScale.FillWidth
                        )

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(600)) + scaleIn(
                                initialScale = 0.85f,
                                animationSpec = tween(600)
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pace_logo_full),
                                contentDescription = "PACE Logo",
                                modifier = Modifier.size(155.dp)
                            )
                        }


                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Discover Your Future",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4E2604),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "One Click at a Time",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4E2604)
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            "Personalized course and career exploration designed to guide students toward the right academic path.",
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = Color(0xFF4E2604),
                            modifier = Modifier.padding(horizontal = 30.dp)
                        )

                        Spacer(Modifier.height(18.dp))

                        Image(
                            painter = painterResource(id = R.drawable.pace_hero_student),
                            contentDescription = "",
                            modifier = Modifier
                                .size(190.dp)
                                .graphicsLayer {
                                    shadowElevation = 20.dp.toPx()
                                    shape = RoundedCornerShape(26.dp)
                                    clip = true
                                }
                        )

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = { showGuestDialog = true },
                            modifier = Modifier
                                .height(52.dp)
                                .width(215.dp),
                            shape = RoundedCornerShape(45.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD62828)
                            )
                        ) {
                            Text("Get Started", color = Color.White, fontSize = 17.sp)
                        }
                    }
                }


                Spacer(Modifier.height(24.dp))


                /* ============================================================
                    PLATFORM FEATURES
                ============================================================ */
                AnimatedSection {
                    Text(
                        "Platform Features",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        "Everything a student needs to identify strengths, match courses and plan a career path.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )

                    FeatureCard(
                        icon = R.drawable.ic_feature_assessment,
                        title = "Smart Career Assessment",
                        desc = "Personality & Interest matching"
                    )

                    FeatureCard(
                        icon = R.drawable.ic_feature_recommendations,
                        title = "Course Recommendations",
                        desc = "Top 3 personalized college course suggestions"
                    )

                    FeatureCard(
                        icon = R.drawable.ic_feature_links,
                        title = "Institution Portal Links",
                        desc = "Connect directly to partnered schools"
                    )
                }


                /* ============================================================
                    ABOUT + MISSION
                ============================================================ */
                AnimatedSection {
                    SectionBlock(
                        title = "About Pace",
                        content =
                            "PACE (Personal Academic & Career Evaluation) is an innovative platform designed to help students discover " +
                                    "their ideal academic paths and future careers — while giving institutions the tools to evaluate, guide, " +
                                    "and track learner progress.\n\nIt combines career assessment, data analytics, and institutional insights " +
                                    "to build a bridge between students’ potential and academic opportunities."
                    )

                    SectionBlock(
                        title = "Our Mission",
                        content =
                            "Our mission is to empower every learner to discover their path and every institution to guide them with purpose. " +
                                    "PACE aims to make academic and career exploration accessible, meaningful, and data-driven.\n\n" +
                                    "We strive to bridge the gap between potential and opportunity, helping students make confident choices " +
                                    "while enabling schools to provide impactful guidance for a smarter future."
                    )
                }

                Spacer(Modifier.height(30.dp))


                Text(
                    "Your Choice, Your Future",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7A3A00)
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    "© 2025 PACE System. All rights reserved.",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 40.dp)
                )
            }
        }
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
