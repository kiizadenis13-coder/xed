package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkYellow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryYellow

@Composable
fun UserProgressOverview(
    overallCompletionRate: Float, // 0.0f to 1.0f
    investmentProgress: Float,
    marketProgress: Float,
    careerProgress: Float,
    streakDays: Int = 14,
    levelNumber: Int = 8,
    levelTitle: String = "Growth Strategist",
    currentXp: Int = 4250,
    targetXp: Int = 5000,
    modifier: Modifier = Modifier
) {
    val animatedOverall by animateFloatAsState(
        targetValue = overallCompletionRate.coerceIn(0f, 1f),
        animationSpec = tween(1000),
        label = "overallProgress"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = null,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Level Badge and Streak Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryYellow.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = PrimaryYellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Level $levelNumber • $levelTitle",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OdometerText(
                                text = "$currentXp",
                                style = MaterialTheme.typography.bodySmall,
                                digitKind = DigitKind.XP,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = " / $targetXp XP to Level ${levelNumber + 1}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Daily Streak Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AccentRed.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OdometerText(
                            text = "$streakDays",
                            style = MaterialTheme.typography.labelMedium,
                            digitKind = DigitKind.STREAK,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = " Days",
                            color = AccentRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // XP Progress Bar
            LinearProgressIndicator(
                progress = { (currentXp.toFloat() / targetXp.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryYellow,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Rings Chart and Multi-Domain Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular 3-Ring Chart
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(110.dp)) {
                        val strokeWidth = 8.dp.toPx()
                        val spacing = 12.dp.toPx()

                        // Ring 1 (Outer - Investment - Red)
                        drawArc(
                            color = AccentRed.copy(alpha = 0.18f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size
                        )
                        drawArc(
                            color = AccentRed,
                            startAngle = -90f,
                            sweepAngle = 360f * (investmentProgress / 100f).coerceIn(0f, 1f),
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size
                        )

                        // Ring 2 (Middle - Market - PrimaryYellow)
                        val inset1 = strokeWidth + spacing / 2
                        val size2 = size.copy(width = size.width - inset1 * 2, height = size.height - inset1 * 2)
                        drawArc(
                            color = PrimaryYellow.copy(alpha = 0.18f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(inset1, inset1),
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size2
                        )
                        drawArc(
                            color = PrimaryYellow,
                            startAngle = -90f,
                            sweepAngle = 360f * (marketProgress / 100f).coerceIn(0f, 1f),
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(inset1, inset1),
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size2
                        )

                        // Ring 3 (Inner - Career - DarkYellow)
                        val inset2 = (strokeWidth + spacing / 2) * 2
                        val size3 = size.copy(width = size.width - inset2 * 2, height = size.height - inset2 * 2)
                        drawArc(
                            color = DarkYellow.copy(alpha = 0.18f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(inset2, inset2),
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size3
                        )
                        drawArc(
                            color = DarkYellow,
                            startAngle = -90f,
                            sweepAngle = 360f * (careerProgress / 100f).coerceIn(0f, 1f),
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(inset2, inset2),
                            style = Stroke(strokeWidth, cap = StrokeCap.Round),
                            size = size3
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OdometerText(
                            text = "${(animatedOverall * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            digitKind = DigitKind.PERCENTAGE
                        )
                        Text(
                            text = "TOTAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 9.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend and metrics
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProgressLegendItem(
                        title = "Investment & Deposits",
                        progress = "${investmentProgress.toInt()}%",
                        color = AccentRed
                    )
                    ProgressLegendItem(
                        title = "Market Shopping",
                        progress = "${marketProgress.toInt()}%",
                        color = PrimaryYellow
                    )
                    ProgressLegendItem(
                        title = "Admin Verified Jobs",
                        progress = "${careerProgress.toInt()}%",
                        color = DarkYellow
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressLegendItem(
    title: String,
    progress: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
        OdometerText(
            text = progress,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            digitKind = DigitKind.PERCENTAGE
        )
    }
}
