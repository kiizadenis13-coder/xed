package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 80-15-5 DESIGN SYSTEM PALETTE FOR "xport" (VIBRANT EMERALD GREEN EDITION)
// 80% Dominant Pitch Black & Charcoal | 15% Signature Electric Emerald Green | 5% Neon Jade & Mint Accents
// ==========================================

// 15% SINGLE BOLD SIGNATURE BRAND COLOR: xport Electric Emerald Green / High-Performance Growth Green
val PrimaryBrand = Color(0xFF00E676)          // Signature xport Electric Emerald Green
val PrimaryBrandDark = Color(0xFF00A344)      // Deep Forest Jade Green
val PrimaryBrandGlow = Color(0x4000E676)      // Radiant Emerald Green Glow
val PrimaryYellow = PrimaryBrand             // Backward compatibility alias (Green)
val PrimaryYellowGlow = PrimaryBrandGlow
val DarkYellow = PrimaryBrandDark

// 5% ACCENTS: High-impact indicators, mint highlights & alerts
val EmeraldSuccess = Color(0xFF00E676)        // Vibrant Emerald
val AccentGreen = Color(0xFF10B981)           // Vivid Jade Green
val NeonGreen = Color(0xFF39FF14)             // Cyber Neon Green
val MintGreen = Color(0xFF6EE7B7)             // Soft Mint Accent
val ForestGreen = Color(0xFF059669)           // Deep Pine Forest
val GreenGlow = Color(0x4D00E676)
val AccentRed = Color(0xFFFF2D55)              // High-contrast secondary red for deletions/alerts
val CrimsonRed = Color(0xFFD50000)
val AmberWarning = Color(0xFFFFB300)
val CyanAccent = Color(0xFF00E5FF)
val IndigoPrimary = Color(0xFF10B981)
val RoseError = Color(0xFFFF1744)

// ICONIC DIGIT PALETTES (Graded from Lowest Decimal Place -> Highest Decimal Place)
// Money digits: Bright Mint Green to Electric Emerald
val MoneyGradientLowest = Color(0xFFA7F3D0)    // Soft Mint (cents / lowest decimal)
val MoneyGradientHighest = Color(0xFF00E676)   // Electric Emerald (highest decimal place)

// XP & Value digits: Pastel Mint to Rich Emerald Jade
val XpGradientLowest = Color(0xFF6EE7B7)       // Bright Mint
val XpGradientHighest = Color(0xFF00A344)      // Deep Forest Jade

// Streak & Momentum digits: Mint to Electric Neon Green
val StreakGradientLowest = Color(0xFFA7F3D0)   // Light Mint
val StreakGradientHighest = Color(0xFF00E676)  // Radiant Emerald Green

// Percentage & Progress digits: Soft Mint to Vibrant Green
val PercentGradientLowest = Color(0xFFD1FAE5)  // Soft Mint
val PercentGradientHighest = Color(0xFF00E676) // Electric Green

// Count & Applicant digits: Vivid Jade to Deep Forest
val CountGradientLowest = Color(0xFF34D399)    // Vivid Jade
val CountGradientHighest = Color(0xFF059669)   // Deep Forest Green

// 80% DOMINANT NEUTRALS: Sleek Obsidian & Deep Pitch Black Canvas with Emerald Undertone
val PitchBlack = Color(0xFF000000)
val PureBlack = Color(0xFF000000)
val DarkBackground = Color(0xFF060907)          // Pure Obsidian Black with subtle emerald undertone
val DarkSurface = Color(0xFF0F1511)             // Primary Card Neutral with subtle forest slate
val DarkSurfaceVariant = Color(0xFF151D18)      // Secondary Card Neutral
val DarkCardBorder = Color.Transparent          // Invisible border everywhere
val DarkElevated = Color(0xFF132319)            // Elevated surface with dark emerald undertone

// High Contrast Neutral Typography
val TextPrimary = Color(0xFFFFFFFF)             // High-contrast pure white
val TextSecondary = Color(0xFFA2B3A7)           // Cool mint-tinted secondary text
val TextMuted = Color(0xFF687C6E)

// Light Theme Neutrals (Emerald & Black High-Contrast Alternative)
val LightBackground = Color(0xFF070B08)
val LightSurface = Color(0xFF0F1712)
val LightSurfaceVariant = Color(0xFF16211A)
val LightCardBorder = Color(0xFF1B3B26)
val LightTextPrimary = Color(0xFFFFFFFF)
val LightTextSecondary = Color(0xFFA2B4A8)

