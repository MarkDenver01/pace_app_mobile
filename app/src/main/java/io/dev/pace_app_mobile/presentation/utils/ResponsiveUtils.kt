package io.dev.pace_app_mobile.presentation.utils

import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.AlertType
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * Responsive height fraction
 *
 * @param fraction
 * @param min
 * @param max
 * @return
 */
@Composable
fun responsiveHeightFraction(
    fraction: Float,
    min: Dp = 100.dp,
    max: Dp = 300.dp
): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val calculated = (screenHeight * fraction).dp
    return calculated.coerceIn(min, max)
}

/**
 * Responsive width fraction
 *
 * @param fraction
 * @param min
 * @param max
 * @return
 */
@Composable
fun responsiveWidthFraction(
    fraction: Float,
    min: Dp = 80.dp,
    max: Dp = 300.dp
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val calculated = (screenWidth * fraction).dp
    return calculated.coerceIn(min, max)
}

/**
 * Responsive text sp
 *
 * @param base
 * @param scaleFactor
 * @param min
 * @param max
 * @return
 */
@Composable
fun responsiveTextSp(
    base: Float = 16f,
    scaleFactor: Float = 0.05f,
    min: Float = 12f,
    max: Float = 28f
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val calculated = (base + (screenWidth * scaleFactor)).coerceIn(min, max)
    return calculated.sp
}

/**
 * Responsive dp
 *
 * @param base
 * @param designWidthDp
 * @return
 */
@Composable
fun responsiveDp(
    base: Float,
    designWidthDp: Float = 360f
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).dp
}

/**
 * Responsive sp
 *
 * @param base
 * @param designWidthDp
 * @return
 */
@Composable
fun responsiveSp(
    base: Float,
    designWidthDp: Float = 360f
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).sp
}


/**
 * Custom text field
 *
 * @param value
 * @param onValueChange
 * @param placeholder
 * @param isPassword
 * @param leadingIcon
 * @param leadingIconPainter
 * @param fontSize
 * @param modifier
 * @receiver
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: ImageVector? = null,
    leadingIconPainter: Painter? = null,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.LightGray,
                fontSize = fontSize
            )
        },
        leadingIcon = {
            when {
                leadingIcon != null -> {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = placeholder,
                        tint = Color(0xFFCC4A1A),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFCC4A1A).copy(alpha = 0.1f))
                            .padding(6.dp)
                    )
                }
                leadingIconPainter != null -> {
                    Icon(
                        painter = leadingIconPainter,
                        contentDescription = placeholder,
                        tint = Color(0xFFCC4A1A),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFCC4A1A).copy(alpha = 0.1f))
                            .padding(6.dp)
                    )
                }
            }
        },
        trailingIcon = {
            if (isPassword) {
                val visibilityIcon =
                    if (passwordVisible) R.drawable.ic_visibility_show else R.drawable.ic_visibility_hide
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = visibilityIcon),
                        contentDescription = description,
                        tint = Color(0xFFCC4A1A)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFCC4A1A).copy(alpha = 0.6f),
            unfocusedBorderColor = Color(0xFFCC4A1A).copy(alpha = 0.3f),
            cursorColor = Color(0xFFCC4A1A),
            focusedTextColor = Color(0xFFCC4A1A),
            unfocusedTextColor = Color(0xFFCC4A1A),
            focusedContainerColor = Color(0xFFCC4A1A).copy(alpha = 0.1f),
            unfocusedContainerColor = Color(0xFFCC4A1A).copy(alpha = 0.05f),
            disabledBorderColor = Color(0xFFCC4A1A).copy(alpha = 0.2f),
            disabledTextColor = Color.Gray,
            disabledContainerColor = Color.LightGray.copy(alpha = 0.1f)
        ),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = LocalTextStyle.current.copy(
            color = Color(0xFFCC4A1A),
            fontSize = fontSize
        ),
        enabled = enabled
    )
}

/**
 * Custom icon button
 *
 * @param icon
 * @param text
 * @param onClick
 * @param width
 * @param height
 * @param fontSize
 * @param backgroundColor
 * @param pressedBackgroundColor
 * @param contentColor
 * @param borderColor
 * @param cornerRadius
 * @param iconTint
 * @param iconSize
 * @param enabled
 * @receiver
 */
@Composable
fun CustomIconButton(
    icon: Int,
    text: String? = null, // null = icon-only
    onClick: () -> Unit,
    width: Dp? = null,
    height: Dp = 48.dp,
    fontSize: TextUnit = 14.sp,
    backgroundColor: Color = Color(0xFFCC4A1A).copy(alpha = 0.08f),
    pressedBackgroundColor: Color = Color(0xFFB23C0F),
    contentColor: Color = Color(0xFFCC4A1A),
    borderColor: Color = Color(0xFFCC4A1A).copy(alpha = 0.4f),
    cornerRadius: Dp = 8.dp,
    iconTint: Color? = null,
    iconSize: Dp = 24.dp,
    enabled: Boolean = true // <-- Added this
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedBg by animateColorAsState(
        if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "IconBtnBg"
    )

    val animatedBorderColor by animateColorAsState(
        if (isPressed) pressedBackgroundColor else borderColor,
        label = "IconBtnBorder"
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled, // <-- Use it here
        interactionSource = interactionSource,
        shape = RoundedCornerShape(cornerRadius),
        border = BorderStroke(1.5.dp, animatedBorderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedBg,
            contentColor = contentColor,
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f), // Optional disabled state
            disabledContentColor = Color.Gray.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .testTag("hello world")
            .then(
                if (width != null) Modifier.width(width).height(height)
                else Modifier.height(height)
            ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = if (enabled) iconTint ?: Color.Unspecified else Color.Gray,
                modifier = Modifier.size(iconSize)
            )
            text?.let {
                Text(
                    text = it,
                    fontSize = fontSize,
                    color = if (enabled) contentColor else Color.Gray
                )
            }
        }
    }
}


/**
 * Custom dynamic button
 *
 * @param onClick
 * @param modifier
 * @param height
 * @param fontSize
 * @param backgroundColor
 * @param pressedBackgroundColor
 * @param cornerRadius
 * @param elevation
 * @param borderColor
 * @param content
 * @receiver
 */
@Composable
fun CustomDynamicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = 48.dp,
    fontSize: TextUnit = 16.sp,
    backgroundColor: Color = Color(0xFFCC4A1A),
    pressedBackgroundColor: Color = Color(0xFFB23C0F),
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 8.dp,
    borderColor: Color = Color.Transparent, // New parameter
    content: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "ButtonBackgroundAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            ), // Border applied here
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedBackgroundColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation,
            focusedElevation = elevation,
            hoveredElevation = elevation,
            disabledElevation = 0.dp
        ),
        interactionSource = interactionSource
    ) {
        Text(
            text = content,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}


/**
 * Custom date time picker
 *
 * @param date
 * @param onDateSelected
 * @param placeholder
 * @param leadingIcon
 * @param fontSize
 * @param modifier
 * @param containerColor
 * @param borderColor
 * @param textColor
 * @receiver
 */
@Composable
fun CustomDateTimePicker(
    date: String,
    onDateSelected: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
    containerColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.3f),
    textColor: Color = Color.White
) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = date,
        onValueChange = {},
        placeholder = {
            Text(
                text = placeholder,
                color = Color.LightGray,
                fontSize = fontSize
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = placeholder,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(6.dp)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        modifier = modifier.clickable { showDialog = true },
        enabled = false,
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = borderColor,
            disabledTextColor = textColor,
            disabledPlaceholderColor = Color.LightGray,
            disabledLeadingIconColor = Color.White,
            disabledContainerColor = containerColor,
            disabledTrailingIconColor = Color.White
        ),
        textStyle = LocalTextStyle.current.copy(
            color = textColor,
            fontSize = fontSize
        )
    )

    if (showDialog) {
        CustomDatePicker(
            onDateSelected = {
                onDateSelected(it)
                showDialog = false
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }
}


/**
 * Custom date picker
 *
 * @param onDateSelected
 * @param onDismissRequest
 * @receiver
 * @receiver
 */
@Composable
fun CustomDatePicker(
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            onDateSelected(format.format(selectedDate.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setOnCancelListener { onDismissRequest() }
    }.show()
}

/**
 * Custom drop down picker
 *
 * @param selectedOption
 * @param onOptionSelected
 * @param options
 * @param placeholder
 * @param leadingIcon
 * @param fontSize
 * @param modifier
 * @param containerColor
 * @param borderColor
 * @param textColor
 * @receiver
 */
@Composable
fun CustomDropDownPicker(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    options: List<String> = listOf("Male", "Female"),
    placeholder: String,
    leadingIcon: ImageVector,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
    containerColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.3f),
    textColor: Color = Color.White
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.LightGray,
                    fontSize = fontSize
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = placeholder,
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(6.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color.White
                )
            },
            singleLine = true,
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp), // prevent double-padding
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = borderColor,
                disabledTextColor = textColor,
                disabledPlaceholderColor = Color.LightGray,
                disabledLeadingIconColor = Color.White,
                disabledTrailingIconColor = Color.White,
                disabledContainerColor = containerColor
            ),
            textStyle = LocalTextStyle.current.copy(
                color = textColor,
                fontSize = fontSize
            )
        )

        Box(
            Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(containerColor)
                .clip(RoundedCornerShape(12.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier
                        .background(Color(0xFF2E7D32)),
                    text = {
                        Text(
                            text = option,
                            color = Color.White,
                            fontSize = fontSize
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }

    }

}

/**
 * Custom dynamic info card
 *
 * @param title
 * @param content
 * @param footerReminder
 * @param modifier
 * @param height
 * @param backgroundColor
 * @param titleFontSize
 * @param bodyFontSize
 * @param footerFontSize
 * @param icon
 * @param iconColor
 */
@Composable
fun CustomDynamicInfoCard(
    title: String,
    content: String,
    footerReminder: String,
    modifier: Modifier = Modifier,
    height: Dp = Dp.Unspecified,
    backgroundColor: Color = Color.White.copy(alpha = 0.06f),
    titleFontSize: TextUnit = LocalResponsiveSizes.current.titleFontSize,
    bodyFontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    footerFontSize: TextUnit = LocalResponsiveSizes.current.labelFontSize,
    icon: ImageVector? = null,
    iconColor: Color = Color.White
) {
    val spacing = LocalAppSpacing.current

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier
            .fillMaxWidth()
            .then(if (height != Dp.Unspecified) Modifier.height(height) else Modifier)
            .padding(horizontal = spacing.md, vertical = spacing.sm)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.md)
        ) {
            // Header: Title + Optional Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier
                            .size(LocalResponsiveSizes.current.iconSize)
                            .padding(end = spacing.sm)
                    )
                }
                Text(
                    text = title,
                    color = LocalAppColors.current.textPrimary,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(spacing.sm))

            // Body content
            Text(
                text = content,
                color = LocalAppColors.current.textPrimary.copy(alpha = 0.95f),
                fontSize = bodyFontSize,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.md))

            // Footer
            Text(
                text = footerReminder,
                color = LocalAppColors.current.textSecondary,
                fontSize = footerFontSize,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Custom check box
 *
 * @param checked
 * @param onCheckedChange
 * @param label
 * @param annotatedLabel
 * @receiver
 */
@Composable
fun CustomCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    annotatedLabel: AnnotatedString? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFB23C0F),
                uncheckedColor = Color.Gray,
                checkmarkColor = Color(0xFFCC4A1A)
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        when {
            annotatedLabel != null -> {
                Text(
                    text = annotatedLabel,
                    color = Color(0xFFCC4A1A)
                )
            }

            label != null -> {
                Text(
                    text = label,
                    color = Color(0xFFCC4A1A)
                )
            }
        }
    }
}

/**
 * Yes no button group
 *
 * @param selected
 * @param onSelect
 * @param modifier
 * @receiver
 */
@Composable
fun YesNoButtonGroup(
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomDynamicButton(
            onClick = { onSelect("YES") },
            modifier = Modifier
                .height(48.dp)
                .weight(1f),
            fontSize = 14.sp,
            backgroundColor = if (selected == "YES") Color(0xFFCC4A1A) else Color( 0xFFCACACA),
            pressedBackgroundColor = if (selected == "YES") Color(0xFFB23C0F) else Color(0xFFD9D9D9),
            cornerRadius = 80.dp, // pill shape (height = 48.dp, radius = height)
            borderColor = if (selected == "YES") Color(0xFFCC4A1A) else Color(0xFF999999),
            content = "YES"
        )

        CustomDynamicButton(
            onClick = { onSelect("NO") },
            modifier = Modifier
                .height(48.dp)
                .weight(1f),
            fontSize = 14.sp,
            backgroundColor = if (selected == "NO") Color(0xFFCC4A1A) else Color( 0xFFCACACA),
            pressedBackgroundColor = if (selected == "NO") Color(0xFFB23C0F) else Color(0xFFD9D9D9),
            cornerRadius = 80.dp,
            borderColor = if (selected == "NO") Color(0xFFCC4A1A) else Color(0xFF999999),
            content = "NO"
        )
    }
}

/**
 * Progress header
 *
 * @param currentIndex
 * @param totalQuestions
 * @param modifier
 */
@Composable
fun ProgressHeader(
    currentIndex: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (totalQuestions > 0) currentIndex / totalQuestions.toFloat() else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "AnimatedProgress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Question ${currentIndex.coerceAtLeast(1)} of $totalQuestions",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF888888), // Subtle gray
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start
        )

        LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
        color = Color(0xFFCC4A1A),
        trackColor = Color(0xFFEFEFEF),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}


/**
 * Alert confirmation dialog
 *
 * @param message
 * @param onConfirm
 * @param onCancel
 * @receiver
 * @receiver
 */
@Composable
fun AlertConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = { /* Prevent outside dismiss */ },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp))
                .background(color = Color(0xFF0076C0), shape = RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onConfirm) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3DDC84)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Confirm", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(onClick = onCancel) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE57373)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Alert dynamic confirmation dialog
 *
 * @param message
 * @param alertType
 * @param onClose
 * @receiver
 */
@Composable
fun AlertDynamicConfirmationDialog(
    message: String,
    alertType: AlertType = AlertType.ERROR,
    onClose: () -> Unit
) {
    val (backgroundColor, pressedColor) = when (alertType) {
        AlertType.SUCCESS -> Pair(Color(0xFF4CAF50), Color(0xFF388E3C)) // Green
        AlertType.WARNING -> Pair(Color(0xFFFFA000), Color(0xFFF57C00)) // Orange
        AlertType.ERROR -> Pair(Color(0xFFD32F2F), Color(0xFFC62828))   // Red
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = message,
                    color = Color.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomDynamicButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    content = "Close",
                    backgroundColor = backgroundColor,
                    pressedBackgroundColor = pressedColor,
                    borderColor = Color.Transparent,
                    fontSize = 16.sp
                )
            }
        }
    }
}


/**
 * Assessment result dialog
 *
 * @param topCourses
 * @param onDismiss
 * @receiver
 */// dummy
@Composable
fun AssessmentResultDialog(
    topCourses: List<String>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Top 3 Recommended Courses", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                topCourses.forEachIndexed { index, course ->
                    Text(text = "${index + 1}. $course", style = MaterialTheme.typography.bodyLarge,
                        color = Color.White)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

/**
 * Custom drop down picker
 *
 * @param selectedOption
 * @param onOptionSelected
 * @param options
 * @param placeholder
 * @param leadingIcon
 * @param modifier
 * @param fontSize
 * @receiver
 */
@Composable
fun CustomDropDownPicker(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier.fillMaxWidth(),
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.LightGray,
                    fontSize = fontSize
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = placeholder,
                    tint = Color(0xFFCC4A1A),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFCC4A1A).copy(alpha = 0.1f))
                        .padding(6.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color(0xFFCC4A1A)
                )
            },
            singleLine = true,
            readOnly = true,
            enabled = false, // Keep this to use the disabled color scheme for the outline
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }, // The clickable modifier must be outside the TextField
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xFFCC4A1A).copy(alpha = 0.3f),
                disabledTextColor = Color(0xFFCC4A1A),
                disabledPlaceholderColor = Color.LightGray,
                disabledLeadingIconColor = Color(0xFFCC4A1A),
                disabledTrailingIconColor = Color(0xFFCC4A1A),
                disabledContainerColor = Color(0xFFCC4A1A).copy(alpha = 0.05f)
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color(0xFFCC4A1A),
                fontSize = fontSize
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(0.95f) // Adjust width to be similar to the text field
                .background(Color(0xFFCC4A1A).copy(alpha = 0.05f))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = Color(0xFFCC4A1A),
                            fontSize = fontSize
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
//                    colors = MenuDefaults.dropdownMenuItemColors(
//                        textColor = Color(0xFFCC4A1A),
//                        trailingIconColor = Color(0xFFCC4A1A)
//                    )
                )
            }
        }
    }
}


/**
 * University dialog
 *
 * @param showDialog
 * @param universities
 * @param selectedUniversityId
 * @param onSelect
 * @param onConfirm
 * @param onDismiss
 * @receiver
 * @receiver
 * @receiver
 */
@Composable
fun UniversityDialog(
    showDialog: Boolean,
    universities: List<UniversityResponse>,
    selectedUniversityId: Long?,
    onSelect: (Long) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Select University") },
            text = {
                var expanded by remember { mutableStateOf(false) }
                var selectedText by remember { mutableStateOf(
                    universities.firstOrNull { it.universityId == selectedUniversityId }?.universityName ?: ""
                ) }

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .background(Color.LightGray)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (selectedText.isEmpty()) "Choose University" else selectedText
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        universities.forEach { uni ->
                            DropdownMenuItem(
                                text = { Text(uni.universityName) },
                                onClick = {
                                    selectedText = uni.universityName
                                    expanded = false
                                    onSelect(uni.universityId)
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}















