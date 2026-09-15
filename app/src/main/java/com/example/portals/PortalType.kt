package com.example.portals

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBrand

enum class PortalType(
    val title: String,
    val subtitle: String,
    val defaultUrl: String,
    val badge: String,
    val themeColorHex: String,
    val accentColor: Color,
    val realPhotoUrl: String
) {
    MARKET(
        title = "Market Shopping Portal",
        subtitle = "Official Store, Electronics, Fashion & Instant Checkout",
        defaultUrl = "https://www.amazon.com",
        badge = "Store Active",
        themeColorHex = "#10B981",
        accentColor = PrimaryBrand,
        realPhotoUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=800&q=80"
    ),
    INVESTMENT(
        title = "Investment & Deposit Portal",
        subtitle = "Deposit funds, 24/7 on-demand instant investing & yield tracking",
        defaultUrl = "https://finance.yahoo.com",
        badge = "Deposit Ready",
        themeColorHex = "#00E676",
        accentColor = EmeraldSuccess,
        realPhotoUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80"
    ),
    SERVICE(
        title = "Service Portal",
        subtitle = "On-demand expert services, verified tasks & instant booking",
        defaultUrl = "https://www.linkedin.com/jobs",
        badge = "Services Active",
        themeColorHex = "#34D399",
        accentColor = AccentGreen,
        realPhotoUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=800&q=80"
    ),
    CUSTOM(
        title = "Live Web Browser",
        subtitle = "Browse external web marketplaces & partner tools",
        defaultUrl = "https://news.google.com",
        badge = "Live Web",
        themeColorHex = "#6EE7B7",
        accentColor = MintGreen,
        realPhotoUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80"
    );

    companion object {
        val JOB = SERVICE
        val INVEST = INVESTMENT
    }

    fun getIcon(): ImageVector {
        return when (this) {
            MARKET -> Icons.Default.ShoppingCart
            INVESTMENT -> Icons.Default.AccountBalance
            SERVICE -> Icons.Default.Work
            CUSTOM -> Icons.Default.Language
        }
    }
}

data class PortalCarouselMedia(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val isVideo: Boolean,
    val durationOrTag: String
)

fun getPortalCarouselMedia(portal: PortalType): List<PortalCarouselMedia> {
    return when (portal) {
        PortalType.MARKET -> listOf(
            PortalCarouselMedia(
                title = "Sony WH-1000XM5 ANC",
                subtitle = "Product Video Pitch • 4K HDR",
                imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80",
                isVideo = true,
                durationOrTag = "0:35 REEL"
            ),
            PortalCarouselMedia(
                title = "UltraWide 4K Studio Display",
                subtitle = "Hardware Showcase Gallery",
                imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=800&q=80",
                isVideo = false,
                durationOrTag = "PHOTO"
            ),
            PortalCarouselMedia(
                title = "Apex Pro Mechanical Board",
                subtitle = "Creator Desk Setup Reel",
                imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=800&q=80",
                isVideo = true,
                durationOrTag = "0:28 REEL"
            )
        )
        PortalType.INVESTMENT -> listOf(
            PortalCarouselMedia(
                title = "Real-Time NAV & Yields",
                subtitle = "Automated Portfolio Rebalancing",
                imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80",
                isVideo = true,
                durationOrTag = "0:45 LIVE"
            ),
            PortalCarouselMedia(
                title = "High-Yield Reserve Fund",
                subtitle = "5.25% APY Daily Compounded",
                imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=800&q=80",
                isVideo = false,
                durationOrTag = "+5.25% APY"
            ),
            PortalCarouselMedia(
                title = "Instant Deposit Gateway",
                subtitle = "Direct ACH & Crypto Vault",
                imageUrl = "https://images.unsplash.com/photo-1526304640581-d334cdbbf45e?auto=format&fit=crop&w=800&q=80",
                isVideo = false,
                durationOrTag = "INSTANT"
            )
        )
        PortalType.SERVICE, PortalType.JOB -> listOf(
            PortalCarouselMedia(
                title = "Mobile & AI Architect",
                subtitle = "Founder Tech Talk & Pitch Reel",
                imageUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=800&q=80",
                isVideo = true,
                durationOrTag = "0:35 VIDEO"
            ),
            PortalCarouselMedia(
                title = "Cloud Infrastructure Lab",
                subtitle = "Silicon Valley Workspace Tour",
                imageUrl = "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=800&q=80",
                isVideo = false,
                durationOrTag = "PHOTO"
            ),
            PortalCarouselMedia(
                title = "Enterprise UI/UX Design Sprint",
                subtitle = "Design System Case Study",
                imageUrl = "https://images.unsplash.com/photo-1531403009284-440f080d1e12?auto=format&fit=crop&w=800&q=80",
                isVideo = true,
                durationOrTag = "0:42 REEL"
            )
        )
        PortalType.CUSTOM -> listOf(
            PortalCarouselMedia(
                title = "Universal Web Connector",
                subtitle = "Multi-platform partner hub",
                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80",
                isVideo = false,
                durationOrTag = "WEB"
            )
        )
    }
}
