package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Desaturated light red palette for the X and Z monogram mark
val DesaturatedLightRed = Color(0xFFE57373)      // Elegant muted light red / salmon coral
val DesaturatedLightRedLight = Color(0xFFEF9A9A) // Soft light red highlight
val DesaturatedLightRedDark = Color(0xFFD32F2F)  // Supporting accent shade

/**
 * Iconic vector mark for xportz:
 * A desaturated light red geometric letter merge of "X" and "Z"
 * framed on a solid pitch black background with NO border (clean & simple).
 */
@Composable
fun XportMark(
    size: Dp = 46.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(size * 0.28f),
                ambientColor = Color(0x44E57373),
                spotColor = Color(0x44E57373)
            )
            .clip(RoundedCornerShape(size * 0.28f))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF000000), // Solid Pitch Black
                        Color(0xFF08080C), // Ultra Deep Obsidian
                        Color(0xFF101014)  // Deep Charcoal
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.72f)) {
            val w = this.size.width
            val h = this.size.height
            val strokeWidth = w * 0.155f

            val glyphBrush = Brush.linearGradient(
                colors = listOf(
                    DesaturatedLightRedLight,
                    DesaturatedLightRed,
                    Color(0xFFEF9A9A)
                ),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )

            // 1. Draw the clean 'Z' glyph:
            // Top horizontal bar -> Main diagonal -> Bottom horizontal bar
            val zPath = Path().apply {
                moveTo(w * 0.18f, h * 0.20f)
                lineTo(w * 0.82f, h * 0.20f)
                lineTo(w * 0.18f, h * 0.80f)
                lineTo(w * 0.82f, h * 0.80f)
            }
            drawPath(
                path = zPath,
                brush = glyphBrush,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 2. Draw the crossing arm to complete the 'X' glyph:
            // Counter-diagonal passing through the center to merge X and Z seamlessly
            drawLine(
                brush = glyphBrush,
                start = Offset(w * 0.18f, h * 0.20f),
                end = Offset(w * 0.82f, h * 0.80f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            // 3. Central geometric focal nexus:
            // Pitch black inset core with a desaturated light red center jewel
            drawCircle(
                color = Color(0xFF000000),
                radius = strokeWidth * 0.55f,
                center = Offset(w * 0.5f, h * 0.5f)
            )
            drawCircle(
                color = DesaturatedLightRedLight,
                radius = strokeWidth * 0.30f,
                center = Offset(w * 0.5f, h * 0.5f)
            )
        }
    }
}

/**
 * Full Iconic Brand Header for "xportz" with desaturated light red typography and credits.
 */
@Composable
fun XportLogo(
    modifier: Modifier = Modifier,
    markSize: Dp = 46.dp,
    showSubtitle: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        XportMark(size = markSize)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "x",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = DesaturatedLightRed,
                    fontSize = (markSize.value * 0.56f).coerceAtLeast(18f).sp,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "port",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = (markSize.value * 0.56f).coerceAtLeast(18f).sp,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "z",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = DesaturatedLightRedLight,
                    fontSize = (markSize.value * 0.56f).coerceAtLeast(18f).sp,
                    letterSpacing = (-0.5).sp
                )
            }
            if (showSubtitle) {
                Text(
                    text = "[from XED.CO] • UGX & USD",
                    style = MaterialTheme.typography.labelSmall,
                    color = DesaturatedLightRedLight,
                    fontSize = (markSize.value * 0.20f).coerceAtLeast(8f).sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.0.sp
                )
            }
        }
    }
}

