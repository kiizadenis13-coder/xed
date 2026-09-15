package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RealtimeAnalyticsState
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkYellow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryYellow
import com.example.util.CurrencyUtils
import java.text.NumberFormat
import java.util.Locale

/**
 * MINIMIZED REAL-TIME TELEMETRY RIBBON
 * Minimizes real-time metrics into a sleek compact 1-row dashboard strip,
 * leaving 3/5 of the home screen dedicated to the Contacts & Chat Hub.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MinimizedKpiBar(
    analyticsState: RealtimeAnalyticsState,
    completedGoals: Int,
    totalGoals: Int,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val numberFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("minimized_kpi_bar"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = null,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header Row: Minimized Title + Live Pulse + Expand Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (analyticsState.activeStreamTicking) PrimaryBrand else AccentRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REAL-TIME TELEMETRY (MINIMIZED)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.8.sp,
                        fontSize = 10.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                ) {
                    Text(
                        text = if (isExpanded) "Collapse" else "Details",
                        fontSize = 10.sp,
                        color = PrimaryBrand,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = PrimaryBrand,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Minimized 1-Row Compact Metric Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Minimized NAV (Showing both Dollars & Uganda Shillings)
                MinimizedPill(
                    icon = Icons.Default.TrendingUp,
                    label = "NAV (USD/UGX)",
                    value = CurrencyUtils.formatCompactDual(analyticsState.portfolioValue),
                    delta = "%.1f%%".format(analyticsState.portfolioDeltaPercent),
                    accentColor = AccentRed,
                    modifier = Modifier.weight(1.3f)
                )

                // 2. Minimized Market
                MinimizedPill(
                    icon = Icons.Default.ShoppingCart,
                    label = "Market",
                    value = "${analyticsState.marketSentimentScore}",
                    delta = "+4%",
                    accentColor = PrimaryYellow,
                    modifier = Modifier.weight(0.9f)
                )

                // 3. Minimized Jobs
                MinimizedPill(
                    icon = Icons.Default.Work,
                    label = "Jobs",
                    value = "6 Live",
                    delta = "+2",
                    accentColor = DarkYellow,
                    modifier = Modifier.weight(0.9f)
                )

                // 4. Minimized Goals
                MinimizedPill(
                    icon = Icons.Default.CheckCircle,
                    label = "Goals",
                    value = "$completedGoals/$totalGoals",
                    delta = "${((completedGoals.toFloat() / maxOf(1, totalGoals)) * 100).toInt()}%",
                    accentColor = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            // Expanded Micro-Details (if tapped)
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "NAV: ${CurrencyUtils.formatDual(analyticsState.portfolioValue)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "1 USD = 3,750 UGX",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryBrand,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stream Status: 24/7 Tick Active",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Frequency: 2000ms Poll",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryBrand,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MinimizedPill(
    icon: ImageVector,
    label: String,
    value: String,
    delta: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = null,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(11.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = delta,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}
