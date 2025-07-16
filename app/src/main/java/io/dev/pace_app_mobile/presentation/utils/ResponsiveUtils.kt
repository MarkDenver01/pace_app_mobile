package io.dev.pace_app_mobile.presentation.utils

import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.ButtonVariants
import io.dev.pace_app_mobile.presentation.theme.LocalAppColors
import io.dev.pace_app_mobile.presentation.theme.LocalAppSpacing
import io.dev.pace_app_mobile.presentation.theme.LocalResponsiveSizes
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

@Composable
fun responsiveDp(
    base: Float,
    designWidthDp: Float = 360f
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).dp
}

@Composable
fun responsiveSp(
    base: Float,
    designWidthDp: Float = 360f
): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scale = screenWidth / designWidthDp
    return (base * scale).sp
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    leadingIcon: ImageVector? = null,
    leadingIconPainter: Painter? = null,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    modifier: Modifier = Modifier
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
            unfocusedContainerColor = Color(0xFFCC4A1A).copy(alpha = 0.05f)
        ),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = LocalTextStyle.current.copy(
            color = Color(0xFFCC4A1A),
            fontSize = fontSize
        )
    )
}

@Composable
fun CustomSocialIconButton(
    icon: Int,
    onClick: () -> Unit,
    size: Dp = 48.dp, // square size
    backgroundColor: Color = Color(0xFFCC4A1A).copy(alpha = 0.08f),
    pressedBackgroundColor: Color = Color(0xFFB23C0F)
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            isPressed =
                interaction is PressInteraction.Press || interaction is HoverInteraction.Enter
        }
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "IconButtonBg"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isPressed) pressedBackgroundColor else Color(0xFFCC4A1A).copy(alpha = 0.4f),
        label = "IconButtonBorder"
    )

    OutlinedButton(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(8.dp), // less round for square look
        border = BorderStroke(1.5.dp, animatedBorderColor),
        modifier = Modifier
            .size(size), // square shape
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedBackgroundColor,
            contentColor = Color(0xFFCC4A1A)
        ),
        contentPadding = PaddingValues(0.dp) // center icon tightly
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun CustomDynamicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = 48.dp,
    fontSize: TextUnit = 16.sp,
    backgroundColor: Color = Color(0xFFCC4A1A),
    pressedBackgroundColor: Color = Color(0xFFB23C0F), // darker on press
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 8.dp,
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
        modifier = modifier.height(height),
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


@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = LocalResponsiveSizes.current.buttonHeight,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    variant: ButtonVariants = ButtonVariants.Filled,
    icon: ImageVector? = null,
    content: String? = null,
    backgroundColor: Color = Color(0xFF2E7D32), // default for Filled
    outlinedBorderColor: Color = Color.White.copy(alpha = 0.4f),
    contentColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Dynamic background based on variant
    val containerColor by animateColorAsState(
        when (variant) {
            ButtonVariants.Filled -> if (isPressed) backgroundColor.darken() else backgroundColor
            ButtonVariants.Outlined -> Color.Transparent
            ButtonVariants.Tonal -> Color.White.copy(alpha = if (isPressed) 0.16f else 0.08f)
        },
        label = "ButtonColorAnimation"
    )

    val borderColor by animateColorAsState(
        when (variant) {
            ButtonVariants.Outlined -> if (isPressed) backgroundColor else outlinedBorderColor
            else -> Color.Transparent
        },
        label = "BorderColorAnimation"
    )

    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (variant == ButtonVariants.Outlined) BorderStroke(
            1.5.dp,
            borderColor
        ) else null,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(fontSize.value.dp + 4.dp)
                )
            }

            if (!content.isNullOrEmpty()) {
                Text(
                    text = content,
                    fontSize = fontSize,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun CustomIconButtonDescription(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = LocalResponsiveSizes.current.buttonHeight,
    fontSize: TextUnit = LocalResponsiveSizes.current.buttonFontSize,
    variant: ButtonVariants = ButtonVariants.Filled,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    title: String? = null,
    description: String? = null,
    backgroundColor: Color = Color(0xFF2E7D32),
    outlinedBorderColor: Color = Color.White.copy(alpha = 0.4f),
    contentColor: Color = Color.White,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val isOutlined = variant == ButtonVariants.Outlined
    val isFilled = variant == ButtonVariants.Filled
    val isTonal = variant == ButtonVariants.Tonal

    // Background color animation
    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color.Gray.copy(alpha = 0.2f)
            isPressed -> backgroundColor.darken()
            isHovered && isOutlined -> backgroundColor.copy(alpha = 0.08f)
            isHovered && isFilled -> backgroundColor.copy(alpha = 0.85f)
            isFilled -> backgroundColor
            isTonal -> Color.White.copy(alpha = if (isPressed) 0.16f else 0.08f)
            isOutlined -> Color.Transparent
            else -> Color.Transparent
        },
        label = "ContainerColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color.LightGray
            isHovered && isOutlined -> backgroundColor
            isPressed && isOutlined -> backgroundColor
            isOutlined -> outlinedBorderColor
            else -> Color.Transparent
        },
        label = "BorderColor"
    )

    val iconTint = if (!enabled) Color.LightGray else contentColor
    val textColor = if (!enabled) Color.LightGray else contentColor

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = textColor,
            disabledContainerColor = containerColor,
            disabledContentColor = textColor
        ),
        border = if (isOutlined) BorderStroke(1.5.dp, borderColor) else null,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                title?.let {
                    Text(
                        text = it,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                }
                description?.let {
                    Text(
                        text = it,
                        fontSize = fontSize * 0.8f,
                        color = textColor.copy(alpha = 0.8f)
                    )
                }
            }

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(fontSize.value.dp + 4.dp)
                )
            } else if (painter != null) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(fontSize.value.dp + 4.dp)
                )
            }
        }
    }
}



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

