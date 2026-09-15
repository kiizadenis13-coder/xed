package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Opening Splash Screen for "xportz":
 * Displays the simple logo mark (no border), app name "xportz",
 * and credits "[from XED.CO]".
 */
@Composable
fun AppSplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.88f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow = infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    LaunchedEffect(Unit) {
        // Smooth entrance animation
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
        // Show for ~2.5 seconds total then seamlessly dismiss
        delay(1800)
        alphaAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 400)
        )
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Allow instant tap to skip
                onSplashFinished()
            }
            .testTag("app_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
                .padding(24.dp)
        ) {
            // Simple App Logo (No Border)
            XportMark(size = 96.dp)

            Spacer(modifier = Modifier.height(28.dp))

            // App Name "xportz"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("splash_app_title")
            ) {
                Text(
                    text = "x",
                    fontWeight = FontWeight.Black,
                    color = DesaturatedLightRed,
                    fontSize = 42.sp,
                    letterSpacing = (-1.0).sp
                )
                Text(
                    text = "port",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 42.sp,
                    letterSpacing = (-1.0).sp
                )
                Text(
                    text = "z",
                    fontWeight = FontWeight.Black,
                    color = DesaturatedLightRedLight,
                    fontSize = 42.sp,
                    letterSpacing = (-1.0).sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Credits: [from XED.CO]
            Surface(
                shape = CircleShape,
                color = Color(0x18FFFFFF),
                modifier = Modifier.testTag("splash_credits")
            ) {
                Text(
                    text = "[from XED.CO]",
                    color = DesaturatedLightRedLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.0.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Subtle telemetry badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(pulseGlow.value * 0.7f)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(DesaturatedLightRed, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MISSION CONTROL • UGX & USD",
                    color = Color(0xFF888898),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}
