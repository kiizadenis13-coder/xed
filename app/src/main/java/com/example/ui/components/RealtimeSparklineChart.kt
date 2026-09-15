package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MetricPoint
import com.example.ui.theme.AccentRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryYellow
import java.text.NumberFormat
import java.util.Locale

enum class TimeRange(val label: String) {
    ONE_HOUR("1H"),
    ONE_DAY("24H"),
    ONE_WEEK("7D"),
    ONE_MONTH("30D"),
    ONE_YEAR("1Y")
}

@Composable
fun RealtimeSparklineChart(
    points: List<MetricPoint>,
    selectedTimeRange: TimeRange,
    onTimeRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier,
    lineColor: Color = PrimaryYellow,
    isLiveTicking: Boolean = true
) {
    var touchX by remember { mutableStateOf<Float?>(null) }
    val numberFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 2 } }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseRadius"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with current live value and delta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "REAL-TIME VALUATION STREAM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        if (isLiveTicking) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    val latestVal = points.lastOrNull()?.value ?: 148520.45
                    val firstVal = points.firstOrNull()?.value ?: latestVal
                    val changePct = if (firstVal > 0) ((latestVal - firstVal) / firstVal) * 100 else 0.0

                    OdometerText(
                        text = numberFormatter.format(latestVal),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OdometerText(
                            text = if (changePct >= 0) "+%.2f%%".format(changePct) else "%.2f%%".format(changePct),
                            style = MaterialTheme.typography.titleSmall,
                            color = if (changePct >= 0) EmeraldSuccess else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "in selected period",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Time Range Switcher Chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TimeRange.entries.forEach { range ->
                        val isSelected = range == selectedTimeRange
                        FilterChip(
                            selected = isSelected,
                            onClick = { onTimeRangeSelected(range) },
                            label = {
                                Text(
                                    text = range.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = null,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .pointerInput(points) {
                        detectTapGestures(
                            onPress = { offset ->
                                touchX = offset.x
                                tryAwaitRelease()
                                touchX = null
                            }
                        )
                    }
                    .pointerInput(points) {
                        detectDragGestures(
                            onDragStart = { offset -> touchX = offset.x },
                            onDrag = { change, _ ->
                                touchX = change.position.x
                            },
                            onDragEnd = { touchX = null },
                            onDragCancel = { touchX = null }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    if (points.size < 2) return@Canvas

                    val minVal = points.minOf { it.value }
                    val maxVal = points.maxOf { it.value }
                    val range = (maxVal - minVal).coerceAtLeast(1.0)
                    val width = size.width
                    val height = size.height

                    val stepX = width / (points.size - 1)

                    val linePath = Path()
                    val fillPath = Path()

                    val chartPoints = points.mapIndexed { index, point ->
                        val x = index * stepX
                        val normalizedY = ((point.value - minVal) / range).toFloat()
                        val y = height - (normalizedY * (height * 0.78f) + (height * 0.11f))
                        Offset(x, y)
                    }

                    linePath.moveTo(chartPoints.first().x, chartPoints.first().y)
                    fillPath.moveTo(chartPoints.first().x, height)
                    fillPath.lineTo(chartPoints.first().x, chartPoints.first().y)

                    // Draw smooth cubic bezier
                    for (i in 0 until chartPoints.size - 1) {
                        val p0 = chartPoints[i]
                        val p1 = chartPoints[i + 1]
                        val controlX = (p0.x + p1.x) / 2f

                        linePath.cubicTo(
                            controlX, p0.y,
                            controlX, p1.y,
                            p1.x, p1.y
                        )
                        fillPath.cubicTo(
                            controlX, p0.y,
                            controlX, p1.y,
                            p1.x, p1.y
                        )
                    }

                    fillPath.lineTo(chartPoints.last().x, height)
                    fillPath.close()

                    // Gradient fill underneath
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.35f),
                                lineColor.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )

                    // Main stroke line
                    drawPath(
                        path = linePath,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw grid lines
                    val gridLines = 3
                    for (i in 1..gridLines) {
                        val y = height * (i.toFloat() / (gridLines + 1))
                        drawLine(
                            color = Color(0xFF334155).copy(alpha = 0.4f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Last point glowing pulse
                    val lastPoint = chartPoints.last()
                    drawCircle(
                        color = lineColor.copy(alpha = 0.25f),
                        radius = pulseRadius.dp.toPx(),
                        center = lastPoint
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 4.dp.toPx(),
                        center = lastPoint
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = lastPoint
                    )

                    // Touch scrubber
                    touchX?.let { tx ->
                        val clampedX = tx.coerceIn(0f, width)
                        val closestIdx = ((clampedX / width) * (points.size - 1)).toInt().coerceIn(0, points.size - 1)
                        val activePoint = chartPoints[closestIdx]

                        drawLine(
                            color = Color.White.copy(alpha = 0.7f),
                            start = Offset(activePoint.x, 0f),
                            end = Offset(activePoint.x, height),
                            strokeWidth = 1.5.dp.toPx()
                        )
                        drawCircle(
                            color = lineColor,
                            radius = 6.dp.toPx(),
                            center = activePoint
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = activePoint
                        )
                    }
                }

                // Scrubber tooltip overlay
                touchX?.let { tx ->
                    val closestIdx = ((tx.coerceIn(0f, 1000f) / 1000f) * (points.size - 1)).toInt().coerceIn(0, points.size - 1)
                    val activeItem = points[closestIdx]

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${activeItem.label}: ",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = numberFormatter.format(activeItem.value),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryYellow
                            )
                        }
                    }
                }
            }
        }
    }
}
