package com.example.data

import com.example.portals.PortalType

/**
 * Account Types that user can switch between in the Account Page:
 * 1. Service Account -> Posts viewed in the Service Portal
 * 2. Sellers Account -> Posts viewed in the Market Portal
 * 3. Founders Account -> Posts viewed in the Invest Portal
 */
enum class UserAccountType(
    val title: String,
    val badge: String,
    val subtitle: String,
    val defaultPortal: PortalType,
    val iconEmoji: String = when (defaultPortal) {
        PortalType.SERVICE -> "🛠️"
        PortalType.MARKET -> "🛍️"
        PortalType.INVESTMENT -> "🚀"
        else -> "💼"
    }
) {
    SERVICE_ACCOUNT(
        title = "Service Account",
        badge = "🛠️ Service Account",
        subtitle = "Offer on-demand services, skilled tasks & freelancing. Posts stream to the Service Portal.",
        defaultPortal = PortalType.SERVICE
    ),
    SELLER_ACCOUNT(
        title = "Sellers Account",
        badge = "🛍️ Sellers Account",
        subtitle = "Sell goods, hardware, fashion & products. Posts stream to the Market Portal.",
        defaultPortal = PortalType.MARKET
    ),
    FOUNDER_ACCOUNT(
        title = "Founders Account",
        badge = "🚀 Founders Account",
        subtitle = "Pitch ventures, raise capital & offer investment vaults. Posts stream to the Invest Portal.",
        defaultPortal = PortalType.INVESTMENT
    );

    val description: String get() = subtitle
    val targetPortalName: String get() = defaultPortal.title
    val portalType: PortalType get() = defaultPortal
}

/**
 * Transport options for delivery in the Market Portal
 */
enum class TransportMeans(
    val title: String,
    val iconEmoji: String,
    val costUsd: Double,
    val eta: String,
    val description: String
) {
    MOTORCYCLE(
        title = "Boda Boda / Motorcycle",
        iconEmoji = "🛵",
        costUsd = 2.50,
        eta = "15-25 min",
        description = "Fastest urban lane splitter"
    ),
    BICYCLE(
        title = "Bicycle Courier",
        iconEmoji = "🚲",
        costUsd = 1.20,
        eta = "30-45 min",
        description = "Eco-friendly, short-distance delivery"
    ),
    TUKTUK(
        title = "Cargo Tricycle / TukTuk",
        iconEmoji = "🛺",
        costUsd = 3.80,
        eta = "25-35 min",
        description = "Mid-sized packages & boxes"
    ),
    DELIVERY_VAN(
        title = "Delivery Van / Pickup",
        iconEmoji = "🚐",
        costUsd = 7.50,
        eta = "35-50 min",
        description = "Heavy goods & multiple parcels"
    ),
    EXPRESS_CAR(
        title = "Sedan Express Car",
        iconEmoji = "🚗",
        costUsd = 5.50,
        eta = "20-40 min",
        description = "Climate-controlled, secure transport"
    ),
    DRONE(
        title = "Drone Air Express",
        iconEmoji = "🛸",
        costUsd = 11.00,
        eta = "10-15 min",
        description = "Direct autonomous air delivery"
    )
}

/**
 * Posts created by users under their specific active account type
 */
data class UserAccountPost(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorAccountType: UserAccountType,
    val targetPortal: PortalType,
    val title: String,
    val description: String,
    val priceOrApyOrRate: Double,
    val priceLabel: String,
    val category: String,
    val photoUrl: String,
    val location: String = "Kampala Central",
    val timeAgo: String = "Just now",
    val tags: List<String> = emptyList()
) {
    val priceOrRate: Double get() = priceOrApyOrRate
}

/**
 * Available Delivery Service accounts near the seller location
 */
data class DeliveryServiceAccount(
    val id: String,
    val name: String,
    val phone: String,
    val transportMode: TransportMeans,
    val distanceKm: Double,
    val rating: Double,
    val completedDeliveries: Int,
    val avatarUrl: String,
    val vehicleNumberPlate: String,
    val isAvailable: Boolean = true
)

/**
 * Order status states for the complete delivery cycle
 */
enum class OrderDeliveryStatus(val label: String, val stepIndex: Int) {
    PENDING_ASSIGNMENT("Awaiting Seller Assignment", 1),
    ASSIGNED_WAITING_PICKUP("Assigned • Awaiting Pickup", 2),
    PICKED_UP_VERIFIED("Picked Up • Proof Verified", 3),
    IN_TRANSIT("In Transit with Live GPS", 4),
    COMPLETED("Delivered & Handed Over", 5)
}

/**
 * Market Delivery Order with live buyer location, seller photo verification,
 * separate transport payment note, and delivery tracking.
 */
data class MarketDeliveryOrder(
    val id: String,
    val productId: String,
    val productTitle: String,
    val productPrice: Double,
    val productPhotoUrl: String,
    val buyerName: String,
    val buyerPhone: String,
    val buyerLocationAddress: String,
    val buyerLatitude: Double,
    val buyerLongitude: Double,
    val sellerName: String,
    val transportMeans: TransportMeans,
    val transportCost: Double,
    val paymentMethodUsed: String,
    val status: OrderDeliveryStatus = OrderDeliveryStatus.PENDING_ASSIGNMENT,
    val assignedServiceAccount: DeliveryServiceAccount? = null,
    val pickupVerificationPhotoUri: String? = null,
    val isPickupAcceptedByService: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
