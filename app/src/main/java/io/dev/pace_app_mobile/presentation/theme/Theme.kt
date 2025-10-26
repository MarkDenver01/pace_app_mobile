package io.dev.pace_app_mobile.presentation.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dev.pace_app_mobile.domain.enums.Customization
import io.dev.pace_app_mobile.presentation.utils.responsiveHeightFraction
import io.dev.pace_app_mobile.presentation.utils.responsiveTextSp
import io.dev.pace_app_mobile.presentation.utils.responsiveWidthFraction
import timber.log.Timber

// ---------------------- Color Schemes (aligned with frontend) ----------------------

data class AppColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val border: Color,
    val placeholder: Color,
    val disabled: Color,
    val pressed: Color,
    val success: Color,
    val error: Color,
)

private val LightColorScheme = AppColorScheme(
    primary = Color(0xFFD94022),      // frontend lightTheme.primary
    background = Color(0xFFF9FAFB),   // frontend lightTheme.background
    onPrimary = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    border = Color(0xFFE0E0E0),
    placeholder = Color(0xFF9E9E9E),
    disabled = Color(0xFFBDBDBD),
    pressed = Color(0xFFBF360C),
    success = Color(0xFF4CAF50),
    error = Color(0xFFF44336)
)

private val DarkColorScheme = AppColorScheme(
    primary = Color(0xFFD94022),      // frontend darkTheme.primary
    background = Color(0xFF121212),   // default dark bg
    onPrimary = Color.White,
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    border = Color(0xFF2C2C2C),
    placeholder = Color(0xFF9E9E9E),
    disabled = Color(0xFF616161),
    pressed = Color(0xFFBF360C),
    success = Color(0xFF81C784),
    error = Color(0xFFE57373)
)

private val RedishColorScheme = AppColorScheme(
    primary = Color(0xFFD32F2F),      // frontend redishTheme.primary
    background = Color(0xFFFFF5F5),   // frontend redishTheme.background
    onPrimary = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    border = Color(0xFFE57373),
    placeholder = Color(0xFFBDBDBD),
    disabled = Color(0xFFE0E0E0),
    pressed = Color(0xFFB71C1C),
    success = Color(0xFF4CAF50),
    error = Color(0xFFF44336)
)

private val BrownishColorScheme = AppColorScheme(
    primary = Color(0xFF8D6E63),      // frontend brownishTheme.primary
    background = Color(0xFFFEFAF5),   // frontend brownishTheme.background
    onPrimary = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    border = Color(0xFFD7CCC8),
    placeholder = Color(0xFF9E9E9E),
    disabled = Color(0xFFBCAAA4),
    pressed = Color(0xFF6D4C41),
    success = Color(0xFF4CAF50),
    error = Color(0xFFF44336)
)

private val PurplelishColorScheme = AppColorScheme(
    primary = Color(0xFF7E57C2),      // frontend purplelishTheme.primary
    background = Color(0xFFF7F3FC),   // frontend purplelishTheme.background
    onPrimary = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    border = Color(0xFFD1C4E9),
    placeholder = Color(0xFF9E9E9E),
    disabled = Color(0xFFBDBDBD),
    pressed = Color(0xFF5E35B1),
    success = Color(0xFF4CAF50),
    error = Color(0xFFF44336)
)

private val GreenishColorScheme = AppColorScheme(
    primary = Color(0xFF00703C),        // La Salle green
    background = Color(0xFFF8FFF8),     // very light green / off-white
    onPrimary = Color.White,             // text/icons on primary
    onBackground = Color.Black,          // text/icons on background
    surface = Color(0xFFE6F4EA),        // pale green surface
    onSurface = Color.Black,             // text/icons on surface
    border = Color(0xFF0B3D2E),         // dark green borders
    placeholder = Color(0xFF4C7A61),    // medium green placeholder
    disabled = Color(0xFF99BFAE),       // light muted green for disabled
    pressed = Color(0xFF005B33),        // darker green for pressed state
    success = Color(0xFF4CAF50),        // keep success green
    error = Color(0xFFF44336)           // keep error red
)

private val BlueishColorScheme = AppColorScheme(
    primary = Color(0xFF003DA5),        // NU blue
    background = Color(0xFFF5F7FF),     // very light blue / off-white
    onPrimary = Color.White,             // text/icons on primary
    onBackground = Color.Black,          // text/icons on background
    surface = Color(0xFFE3E9FF),        // pale blue surface
    onSurface = Color.Black,             // text/icons on surface
    border = Color(0xFF00297A),         // dark blue borders
    placeholder = Color(0xFF3366CC),    // medium blue placeholder
    disabled = Color(0xFF99B3E6),       // light muted blue for disabled
    pressed = Color(0xFF002270),        // darker blue for pressed state
    success = Color(0xFF4CAF50),        // keep success green
    error = Color(0xFFF44336)           // keep error red
)

private val MaroonishColorScheme = AppColorScheme(
    primary = Color(0xFFD50032),        // LPU red
    background = Color(0xFFFFF5F5),     // very light red / off-white
    onPrimary = Color.White,             // text/icons on primary
    onBackground = Color.Black,          // text/icons on background
    surface = Color(0xFFFFE5E9),        // pale red/pink surface
    onSurface = Color.Black,             // text/icons on surface
    border = Color(0xFF7A001F),         // dark red borders
    placeholder = Color(0xFFB20027),    // medium red placeholder
    disabled = Color(0xFFFF99A3),       // light muted red for disabled
    pressed = Color(0xFF8C0026),        // darker red for pressed state
    success = Color(0xFF4CAF50),        // keep success green
    error = Color(0xFFF44336)           // keep error red
)


fun AppColorScheme.toMaterialColors(darkTheme: Boolean = false): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            error = error,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            error = error,
            onError = Color.White,
        )
    }
}

// ---------------------- Custom Locals ----------------------
val LocalAppColors = staticCompositionLocalOf { LightColorScheme }

data class AppSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val lsm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

data class AppTypography(
    val titleLarge: TextStyle = TextStyle(fontSize = 22.sp),
    val body: TextStyle = TextStyle(fontSize = 16.sp),
    val caption: TextStyle = TextStyle(fontSize = 12.sp)
)

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

data class ResponsiveSizes(
    val logoSize: Dp,
    val buttonHeight: Dp,
    val buttonWidth: Dp,
    val titleFontSize: TextUnit,
    val buttonFontSize: TextUnit,
    val paddingHorizontal: Dp,
    val paddingVertical: Dp,
    val iconSize: Dp,
    val labelFontSize: TextUnit,
    val fontLargeSizeLarge: TextUnit,
    val smallFontSize: TextUnit
)

val LocalResponsiveSizes = staticCompositionLocalOf {
    ResponsiveSizes(
        logoSize = 200.dp,
        buttonHeight = 64.dp,
        buttonWidth = 170.dp,
        titleFontSize = 24.sp,
        buttonFontSize = 16.sp,
        paddingHorizontal = 24.dp,
        paddingVertical = 32.dp,
        iconSize = 20.dp,
        labelFontSize = 16.sp,
        fontLargeSizeLarge = 32.sp,
        smallFontSize = 14.sp
    )
}

// ---------------------- Theme ----------------------

@Composable
fun Pace_app_mobileTheme(
    customization: Customization = Customization.lightTheme,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    Timber.e("xxxxx: ${customization.name}")
    val colorScheme = when (customization) {
        Customization.darkTheme -> {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // fallback to custom dark scheme if dynamic colors unavailable
                DarkColorScheme
            } else DarkColorScheme
        }

        Customization.lightTheme -> {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // fallback to custom light scheme if dynamic colors unavailable
                LightColorScheme
            } else LightColorScheme
        }

        Customization.redishTheme -> RedishColorScheme
        Customization.purplelishTheme -> PurplelishColorScheme
        Customization.brownishTheme -> BrownishColorScheme
        Customization.greenishTheme -> GreenishColorScheme
        Customization.blueishTheme -> BlueishColorScheme
        Customization.maroonishTheme -> MaroonishColorScheme
    }

    val responsiveSizes = ResponsiveSizes(
        logoSize = responsiveHeightFraction(0.4f, min = 160.dp, max = 320.dp),
        buttonHeight = responsiveHeightFraction(0.07f, min = 50.dp, max = 76.dp),
        buttonWidth = responsiveWidthFraction(0.45f, min = 140.dp, max = 240.dp),
        titleFontSize = responsiveTextSp(base = 18f, scaleFactor = 0.03f, min = 14f, max = 28f),
        buttonFontSize = responsiveTextSp(base = 14f, scaleFactor = 0.025f, min = 12f, max = 22f),
        paddingHorizontal = responsiveWidthFraction(0.06f, min = 16.dp, max = 32.dp),
        paddingVertical = responsiveHeightFraction(0.05f, min = 16.dp, max = 40.dp),
        iconSize = responsiveWidthFraction(0.07f, min = 18.dp, max = 32.dp),
        labelFontSize = responsiveTextSp(base = 18f, scaleFactor = 0.03f, min = 14f, max = 28f),
        fontLargeSizeLarge = responsiveTextSp(
            base = 24f,
            scaleFactor = 0.03f,
            min = 20f,
            max = 32f
        ),
        smallFontSize = responsiveTextSp(base = 14f, scaleFactor = 0.03f, min = 10f, max = 14f)
    )

    CompositionLocalProvider(
        LocalResponsiveSizes provides responsiveSizes,
        LocalAppColors provides colorScheme,
        LocalAppSpacing provides AppSpacing(),
        LocalAppTypography provides AppTypography()
    ) {
        MaterialTheme(
            colorScheme = colorScheme.toMaterialColors(
                darkTheme = (customization == Customization.darkTheme)
            ),
            typography = Typography,
            content = content
        )
    }
}
