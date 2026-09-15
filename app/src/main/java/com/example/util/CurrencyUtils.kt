package com.example.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import java.text.NumberFormat
import java.util.Locale

/**
 * Centralized currency converter & dual-currency formatting utility
 * supporting both Uganda Shillings (UGX) and US Dollars (USD / $).
 * Standard exchange rate reference: 1 USD = 3,750 UGX.
 */
object CurrencyUtils {
    const val UGX_PER_USD: Double = 3750.0

    private val ugxIntegerFormat: NumberFormat = NumberFormat.getIntegerInstance(Locale.US)
    private val usdNumberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    fun usdToUgx(amountUsd: Double): Double = amountUsd * UGX_PER_USD

    fun ugxToUsd(amountUgx: Double): Double = amountUgx / UGX_PER_USD

    /** Formats USD amount, e.g. "$12.50" */
    fun formatUsd(amountUsd: Double): String {
        return usdNumberFormat.format(amountUsd)
    }

    /** Formats UGX amount with currency prefix, e.g. "UGX 46,875" */
    fun formatUgx(amountUsd: Double): String {
        val ugx = usdToUgx(amountUsd)
        return "UGX " + ugxIntegerFormat.format(ugx.toLong())
    }

    /** Formats raw integer UGX without "UGX " prefix, e.g. "46,875" */
    fun formatUgxRaw(amountUsd: Double): String {
        val ugx = usdToUgx(amountUsd)
        return ugxIntegerFormat.format(ugx.toLong())
    }

    /** Formats dual currency inline: "$12.50 • UGX 46,875" */
    fun formatDual(amountUsd: Double, separator: String = " • "): String {
        return "${formatUsd(amountUsd)}$separator${formatUgx(amountUsd)}"
    }

    /** Formats dual currency with parentheses: "$12.50 (UGX 46,875)" */
    fun formatDualParens(amountUsd: Double): String {
        return "${formatUsd(amountUsd)} (${formatUgx(amountUsd)})"
    }

    /** Formats compact dual currency for badges: "$12 • 46.8K UGX" */
    fun formatCompactDual(amountUsd: Double): String {
        val ugx = usdToUgx(amountUsd)
        val ugxCompact = when {
            ugx >= 1_000_000_000 -> String.format(Locale.US, "%.1fB", ugx / 1_000_000_000)
            ugx >= 1_000_000 -> String.format(Locale.US, "%.1fM", ugx / 1_000_000)
            ugx >= 1_000 -> String.format(Locale.US, "%.1fK", ugx / 1_000)
            else -> ugx.toInt().toString()
        }
        val usdCompact = if (amountUsd >= 1000) {
            String.format(Locale.US, "$%.1fK", amountUsd / 1000)
        } else {
            String.format(Locale.US, "$%.0f", amountUsd)
        }
        return "$usdCompact • UGX $ugxCompact"
    }

    /** Formats rate with unit, e.g. "$45.00/hr • UGX 168,750/hr" */
    fun formatDualRate(amountUsd: Double, unit: String = "/hr"): String {
        val ugx = usdToUgx(amountUsd)
        val ugxFormatted = ugxIntegerFormat.format(ugx.toLong())
        return "$${String.format(Locale.US, "%.0f", amountUsd)}$unit • UGX $ugxFormatted$unit"
    }
}

/**
 * Reusable Composable for displaying dual currency (Dollars & Uganda Shillings)
 * with visual hierarchy and distinct color coding.
 */
@Composable
fun DualCurrencyBadge(
    amountUsd: Double,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
    fontSize: TextUnit = 11.sp
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isHighlighted) EmeraldSuccess.copy(alpha = 0.2f) else Color(0x33FFFFFF),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = CurrencyUtils.formatUsd(amountUsd),
                color = if (isHighlighted) EmeraldSuccess else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "•",
                color = Color(0xFF888898),
                fontSize = (fontSize.value * 0.8f).sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = CurrencyUtils.formatUgx(amountUsd),
                color = if (isHighlighted) Color(0xFF81C784) else Color(0xFFB0B0C0),
                fontWeight = FontWeight.SemiBold,
                fontSize = (fontSize.value * 0.9f).sp
            )
        }
    }
}

/**
 * Stacked Dual Currency display for large KPI cards or header balances.
 */
@Composable
fun DualCurrencyStacked(
    amountUsd: Double,
    modifier: Modifier = Modifier,
    primaryColor: Color = Color.White,
    secondaryColor: Color = EmeraldSuccess,
    primaryFontSize: TextUnit = 20.sp,
    secondaryFontSize: TextUnit = 12.sp,
    alignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            text = CurrencyUtils.formatUsd(amountUsd),
            color = primaryColor,
            fontSize = primaryFontSize,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "≈ ${CurrencyUtils.formatUgx(amountUsd)}",
            color = secondaryColor,
            fontSize = secondaryFontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
