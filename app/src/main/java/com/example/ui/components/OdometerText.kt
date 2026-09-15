package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentRed
import com.example.ui.theme.CountGradientHighest
import com.example.ui.theme.CountGradientLowest
import com.example.ui.theme.MoneyGradientHighest
import com.example.ui.theme.MoneyGradientLowest
import com.example.ui.theme.PercentGradientHighest
import com.example.ui.theme.PercentGradientLowest
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryBrandDark
import com.example.ui.theme.StreakGradientHighest
import com.example.ui.theme.StreakGradientLowest
import com.example.ui.theme.XpGradientHighest
import com.example.ui.theme.XpGradientLowest

/**
 * Iconic Digit Classification for Precision Gradient Odometers
 */
enum class DigitKind {
    MONEY,       // Emerald Mint (lowest decimal) -> Rich Electric Emerald (highest decimal)
    XP,          // Bright Volt (lowest digit) -> Deep Amber (highest digit)
    PERCENTAGE,  // Ice Cyan (lowest decimal) -> Electric Azure (highest decimal)
    STREAK,      // Light Coral (lowest digit) -> Flaming Crimson (highest digit)
    COUNT,       // Soft Neon Violet (lowest digit) -> Deep Purple (highest digit)
    DELTA_POS,   // Neon Mint (lowest decimal) -> Cyber Gold (highest decimal)
    DELTA_NEG,   // Coral Pink (lowest decimal) -> Electric Crimson (highest decimal)
    BRAND,       // Cyber Gold (lowest digit) -> Rich Amber (highest digit)
    CUSTOM,      // Uses provided gradientColors
    NONE         // Solid single color
}

/**
 * Linear color interpolation between two colors.
 */
private fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    val clamped = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (end.red - start.red) * clamped,
        green = start.green + (end.green - start.green) * clamped,
        blue = start.blue + (end.blue - start.blue) * clamped,
        alpha = start.alpha + (end.alpha - start.alpha) * clamped
    )
}

/**
 * High-craft Odometer Display Component for "xport".
 *
 * Each individual digit rolls vertically on change with an authentic mechanical odometer effect.
 * When rolling/rendering, digit colors follow a continuous gradient spanning from the
 * lowest decimal place (rightmost) to the highest decimal place (leftmost).
 */
@Composable
fun OdometerText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    digitKind: DigitKind = DigitKind.NONE,
    gradientColors: List<Color>? = null,
    gradientBrush: Brush? = null,
    useGradientOnDigits: Boolean = false,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily? = null,
    animationDurationMillis: Int = 420
) {
    // Determine the lowest (rightmost) and highest (leftmost) gradient color stops
    val colorPair: Pair<Color, Color>? = when {
        digitKind == DigitKind.MONEY -> Pair(MoneyGradientLowest, MoneyGradientHighest)
        digitKind == DigitKind.XP -> Pair(XpGradientLowest, XpGradientHighest)
        digitKind == DigitKind.PERCENTAGE -> Pair(PercentGradientLowest, PercentGradientHighest)
        digitKind == DigitKind.STREAK -> Pair(StreakGradientLowest, StreakGradientHighest)
        digitKind == DigitKind.COUNT -> Pair(CountGradientLowest, CountGradientHighest)
        digitKind == DigitKind.DELTA_POS -> Pair(MoneyGradientLowest, PrimaryBrand)
        digitKind == DigitKind.DELTA_NEG -> Pair(StreakGradientLowest, AccentRed)
        digitKind == DigitKind.BRAND -> Pair(PrimaryBrand, PrimaryBrandDark)
        gradientColors != null && gradientColors.size >= 2 -> Pair(gradientColors.first(), gradientColors.last())
        useGradientOnDigits -> Pair(PrimaryBrand, AccentRed)
        text.startsWith("$") -> Pair(MoneyGradientLowest, MoneyGradientHighest)
        text.endsWith("%") -> Pair(PercentGradientLowest, PercentGradientHighest)
        text.startsWith("+") -> Pair(MoneyGradientLowest, PrimaryBrand)
        text.startsWith("-") -> Pair(StreakGradientLowest, AccentRed)
        else -> null
    }
    val lowestColor = colorPair?.first
    val highestColor = colorPair?.second

    val baseStyle = style.copy(
        fontWeight = fontWeight ?: style.fontWeight,
        fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
        fontFamily = fontFamily ?: style.fontFamily
    )

    // Count total digits to establish fractional gradient positions
    val totalDigits = text.count { it.isDigit() }

    // Resolve non-digit symbol color (e.g. '$', '%', '+', '-')
    val symbolColor = when {
        lowestColor != null -> highestColor ?: lowestColor
        color != Color.Unspecified -> color
        else -> style.color
    }

    val symbolStyle = baseStyle.copy(color = symbolColor)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var currentDigitIndex = 0
        text.forEach { char ->
            if (char.isDigit()) {
                // fraction = 0.0 at lowest decimal place (rightmost), 1.0 at highest decimal place (leftmost)
                val fraction = if (totalDigits <= 1) {
                    0.5f
                } else {
                    (totalDigits - 1 - currentDigitIndex).toFloat() / (totalDigits - 1).toFloat()
                }

                val digitColor = if (lowestColor != null && highestColor != null) {
                    lerpColor(start = lowestColor, end = highestColor, fraction = fraction)
                } else if (gradientBrush != null) {
                    null
                } else if (color != Color.Unspecified) {
                    color
                } else {
                    style.color
                }

                val effectiveDigitStyle = if (digitColor != null) {
                    baseStyle.copy(color = digitColor)
                } else if (gradientBrush != null) {
                    baseStyle.copy(brush = gradientBrush)
                } else {
                    baseStyle.copy(color = style.color)
                }

                // Mechanical cascade: lower decimal place spins faster, higher decimal place spins with slight offset
                val staggeredDuration = (animationDurationMillis - ((totalDigits - 1 - currentDigitIndex) * 12))
                    .coerceAtLeast(280)

                OdometerDigit(
                    char = char,
                    style = effectiveDigitStyle,
                    animationDurationMillis = staggeredDuration
                )

                currentDigitIndex++
            } else {
                Text(
                    text = char.toString(),
                    style = symbolStyle
                )
            }
        }
    }
}

@Composable
fun OdometerDigit(
    char: Char,
    style: TextStyle,
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = 400
) {
    AnimatedContent(
        targetState = char,
        transitionSpec = {
            val direction = if (targetState >= initialState) 1 else -1
            (slideInVertically(
                animationSpec = tween(animationDurationMillis, easing = FastOutSlowInEasing),
                initialOffsetY = { fullHeight -> fullHeight * direction }
            ) + fadeIn(
                animationSpec = tween(animationDurationMillis / 2)
            )).togetherWith(
                slideOutVertically(
                    animationSpec = tween(animationDurationMillis, easing = FastOutSlowInEasing),
                    targetOffsetY = { fullHeight -> -fullHeight * direction }
                ) + fadeOut(
                    animationSpec = tween(animationDurationMillis / 2)
                )
            ).using(SizeTransform(clip = true))
        },
        label = "odometer_digit_roll",
        modifier = modifier.clipToBounds()
    ) { digitChar ->
        Text(
            text = digitChar.toString(),
            style = style,
            textAlign = TextAlign.Center
        )
    }
}
