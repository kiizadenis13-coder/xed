package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portal_cache")
data class PortalCacheEntity(
    @PrimaryKey
    val portalKey: String, // "MARKET", "SERVICE", "INVESTMENT", "CUSTOM"
    val title: String,
    val subtitle: String,
    val badge: String,
    val themeColorHex: String,
    val realPhotoUrl: String,
    val lastCachedTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "market_products")
data class MarketProductEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val sellerAccountName: String,
    val sellerAccountHandle: String,
    val sellerRating: Float,
    val sellerSalesCount: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int,
    val stockLeft: Int,
    val rating: Float,
    val reviewCount: Int,
    val description: String,
    val specsFormatted: String, // formatted as "Label:Value||Label:Value"
    val shippingTime: String = "Free 1-Day Air Delivery",
    val warrantyText: String = "2-Year Manufacturer Warranty",
    val primaryIconEmoji: String = "⚡",
    val imageUrlsFormatted: String, // separated by "||"
    val videoPreviewUrl: String? = null,
    val defaultMediaType: String = "VIDEO", // "VIDEO" or "IMAGE"
    val themeAccentHex: String = "#10B981"
)

@Entity(tableName = "portal_carousel_media")
data class PortalCarouselMediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val portalType: String, // "MARKET", "SERVICE", "INVESTMENT", "CUSTOM"
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val isVideo: Boolean,
    val durationOrTag: String,
    val orderIndex: Int = 0
)
