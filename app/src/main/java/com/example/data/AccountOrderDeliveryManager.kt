package com.example.data

import com.example.portals.PortalType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * System notification sent to users (Seller, Service Provider, or Buyer)
 */
data class AppSystemNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val targetRole: String, // "BUYER", "SELLER", "SERVICE_PROVIDER"
    val timestamp: Long = System.currentTimeMillis(),
    val relatedOrderId: String? = null
)

class AccountOrderDeliveryManager(private val scope: CoroutineScope) {

    // Available Delivery Service Providers near location
    private val _availableServiceAccounts = MutableStateFlow<List<DeliveryServiceAccount>>(
        listOf(
            DeliveryServiceAccount(
                id = "srv_boda_1",
                name = "Boda Express Pro (Denis Mukasa)",
                phone = "+256 701 445 889",
                transportMode = TransportMeans.MOTORCYCLE,
                distanceKm = 0.8,
                rating = 4.94,
                completedDeliveries = 1420,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "UFA 889K"
            ),
            DeliveryServiceAccount(
                id = "srv_boda_2",
                name = "Kampala Moto Rush (Ronald Ssebuwufu)",
                phone = "+256 772 119 034",
                transportMode = TransportMeans.MOTORCYCLE,
                distanceKm = 1.1,
                rating = 4.88,
                completedDeliveries = 890,
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "UED 234L"
            ),
            DeliveryServiceAccount(
                id = "srv_bike_1",
                name = "Green Velocity Couriers (David Okello)",
                phone = "+256 788 902 114",
                transportMode = TransportMeans.BICYCLE,
                distanceKm = 0.5,
                rating = 4.97,
                completedDeliveries = 640,
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "BIKE-09"
            ),
            DeliveryServiceAccount(
                id = "srv_tuktuk_1",
                name = "Cargo TukTuk Express (Sarah Nalwanga)",
                phone = "+256 755 832 991",
                transportMode = TransportMeans.TUKTUK,
                distanceKm = 1.4,
                rating = 4.85,
                completedDeliveries = 1120,
                avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "UTK 567M"
            ),
            DeliveryServiceAccount(
                id = "srv_van_1",
                name = "Metro Haul Heavy Van (Alex Kato Logistics)",
                phone = "+256 702 334 551",
                transportMode = TransportMeans.DELIVERY_VAN,
                distanceKm = 2.0,
                rating = 4.92,
                completedDeliveries = 2300,
                avatarUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "UBB 102T"
            ),
            DeliveryServiceAccount(
                id = "srv_car_1",
                name = "Prestige Sedan Courier (Brian Musoke)",
                phone = "+256 774 556 778",
                transportMode = TransportMeans.EXPRESS_CAR,
                distanceKm = 1.8,
                rating = 4.90,
                completedDeliveries = 750,
                avatarUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "UAY 442C"
            ),
            DeliveryServiceAccount(
                id = "srv_drone_1",
                name = "SkyDrop Autonomous Drone Port #04",
                phone = "+256 800 123 456",
                transportMode = TransportMeans.DRONE,
                distanceKm = 0.3,
                rating = 4.99,
                completedDeliveries = 3450,
                avatarUrl = "https://images.unsplash.com/photo-1527977966376-1c8408f9f108?auto=format&fit=crop&w=200&q=80",
                vehicleNumberPlate = "DRONE-UG-04"
            )
        )
    )
    val availableServiceAccounts: StateFlow<List<DeliveryServiceAccount>> = _availableServiceAccounts.asStateFlow()

    // User Posts segregated by Target Portal
    private val _userPosts = MutableStateFlow<List<UserAccountPost>>(
        listOf(
            // Service Account Post -> viewed in SERVICE PORTAL
            UserAccountPost(
                id = "post_srv_1",
                authorId = "usr_001",
                authorName = "Denis Kiiza (Service Pro)",
                authorAccountType = UserAccountType.SERVICE_ACCOUNT,
                targetPortal = PortalType.SERVICE,
                title = "Expert Cloud Architecture & Android Full-Stack Systems",
                description = "On-demand system design, high-availability microservices, Kotlin Jetpack Compose development, and cloud infrastructure setup.",
                priceOrApyOrRate = 75.0,
                priceLabel = "$75 / hr",
                category = "Engineering & Software",
                photoUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=800&q=80",
                location = "Kampala Central / Remote",
                timeAgo = "10m ago",
                tags = listOf("Kotlin", "Cloud", "Architecture", "Consulting")
            ),
            UserAccountPost(
                id = "post_srv_2",
                authorId = "usr_002",
                authorName = "SolarMaster Uganda (Service Account)",
                authorAccountType = UserAccountType.SERVICE_ACCOUNT,
                targetPortal = PortalType.SERVICE,
                title = "Smart Solar Backup & Hybrid Inverter Installation",
                description = "Residential & commercial clean energy setup with live telemetry, lithium battery backup, and 24/7 priority maintenance.",
                priceOrApyOrRate = 120.0,
                priceLabel = "$120 flat fee",
                category = "Home & Energy",
                photoUrl = "https://images.unsplash.com/photo-1509391365360-2e959784a276?auto=format&fit=crop&w=800&q=80",
                location = "Greater Kampala Metro",
                timeAgo = "1h ago",
                tags = listOf("Solar", "Hardware", "Certified", "Warranty")
            ),
            // Sellers Account Post -> viewed in MARKET PORTAL
            UserAccountPost(
                id = "post_seller_1",
                authorId = "usr_001",
                authorName = "Denis Kiiza Official Store (Sellers Account)",
                authorAccountType = UserAccountType.SELLER_ACCOUNT,
                targetPortal = PortalType.MARKET,
                title = "HyperSound Elite Active Noise-Cancelling Headphones",
                description = "Audiophile-grade studio acoustic response, 60-hour ultra battery, dual wireless lossless codec, and titanium alloy headband.",
                priceOrApyOrRate = 189.0,
                priceLabel = "$189.00",
                category = "Electronics",
                photoUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80",
                location = "Kampala Tech Hub",
                timeAgo = "25m ago",
                tags = listOf("Audio", "Wireless", "ANC", "Best Seller")
            ),
            UserAccountPost(
                id = "post_seller_2",
                authorId = "usr_003",
                authorName = "Nile Gear Outfitters (Sellers Account)",
                authorAccountType = UserAccountType.SELLER_ACCOUNT,
                targetPortal = PortalType.MARKET,
                title = "Rugged Titanium Multisport Smartwatch V4",
                description = "Sapphire crystal display, offline topographic maps, optical cardiac telemetry, and solar charging rim.",
                priceOrApyOrRate = 249.0,
                priceLabel = "$249.00",
                category = "Wearables",
                photoUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80",
                location = "Kampala Central Store",
                timeAgo = "2h ago",
                tags = listOf("Wearable", "GPS", "Titanium", "Waterproof")
            ),
            // Founders Account Post -> viewed in INVEST PORTAL
            UserAccountPost(
                id = "post_founder_1",
                authorId = "usr_001",
                authorName = "Denis Kiiza (Founders Account)",
                authorAccountType = UserAccountType.FOUNDER_ACCOUNT,
                targetPortal = PortalType.INVESTMENT,
                title = "AfriClean Solar Micro-Grids Series Seed Syndicate",
                description = "Decentralized solar micro-grids powering 45 rural agro-hubs across East Africa with automated tokenized revenue share.",
                priceOrApyOrRate = 24.5,
                priceLabel = "+24.5% APY",
                category = "CleanTech & Infrastructure",
                photoUrl = "https://images.unsplash.com/photo-1497435334941-8c899ee9e8e9?auto=format&fit=crop&w=800&q=80",
                location = "Kampala Innovation Hub",
                timeAgo = "5m ago",
                tags = listOf("Seed", "CleanTech", "High Yield", "Verified Founder")
            ),
            UserAccountPost(
                id = "post_founder_2",
                authorId = "usr_004",
                authorName = "PaySwift FinTech (Founders Account)",
                authorAccountType = UserAccountType.FOUNDER_ACCOUNT,
                targetPortal = PortalType.INVESTMENT,
                title = "Cross-Border Liquidity Liquidity Pool V2",
                description = "Automated cross-border settlement liquidity engine providing sub-second fiat-to-crypto bridging across Africa.",
                priceOrApyOrRate = 19.8,
                priceLabel = "+19.8% APY",
                category = "FinTech",
                photoUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=800&q=80",
                location = "FinTech Hub",
                timeAgo = "3h ago",
                tags = listOf("FinTech", "Liquidity", "Audited", "Defi")
            )
        )
    )
    val userPosts: StateFlow<List<UserAccountPost>> = _userPosts.asStateFlow()

    // Active Market Orders & Deliveries
    private val _orders = MutableStateFlow<List<MarketDeliveryOrder>>(emptyList())
    val orders: StateFlow<List<MarketDeliveryOrder>> = _orders.asStateFlow()

    // System Notifications Broadcast
    private val _notifications = MutableSharedFlow<AppSystemNotification>(extraBufferCapacity = 64)
    val notifications: SharedFlow<AppSystemNotification> = _notifications.asSharedFlow()

    private val _latestToast = MutableStateFlow<String?>(null)
    val latestToast: StateFlow<String?> = _latestToast.asStateFlow()

    fun dismissToast() {
        _latestToast.value = null
    }

    fun publishAccountPost(
        accountType: UserAccountType,
        authorName: String,
        authorPhotoUrl: String = "",
        title: String,
        description: String,
        priceOrRate: Double,
        priceLabel: String,
        category: String,
        photoUrl: String
    ): UserAccountPost {
        return createPost(
            authorName = authorName,
            accountType = accountType,
            title = title,
            description = description,
            priceOrRate = priceOrRate,
            priceLabel = priceLabel,
            category = category,
            photoUrl = photoUrl
        )
    }

    /**
     * Create a new post from the active user's account type.
     * Posts from SERVICE_ACCOUNT go to SERVICE portal
     * Posts from SELLER_ACCOUNT go to MARKET portal
     * Posts from FOUNDER_ACCOUNT go to INVEST portal
     */
    fun createPost(
        authorName: String,
        accountType: UserAccountType,
        title: String,
        description: String,
        priceOrRate: Double,
        priceLabel: String,
        category: String,
        photoUrl: String
    ): UserAccountPost {
        val targetPortal = when (accountType) {
            UserAccountType.SERVICE_ACCOUNT -> PortalType.SERVICE
            UserAccountType.SELLER_ACCOUNT -> PortalType.MARKET
            UserAccountType.FOUNDER_ACCOUNT -> PortalType.INVESTMENT
        }

        val newPost = UserAccountPost(
            id = "post_${System.currentTimeMillis()}",
            authorId = "usr_001",
            authorName = authorName,
            authorAccountType = accountType,
            targetPortal = targetPortal,
            title = title,
            description = description,
            priceOrApyOrRate = priceOrRate,
            priceLabel = priceLabel,
            category = category,
            photoUrl = photoUrl.ifBlank { targetPortal.realPhotoUrl },
            location = "Kampala Central",
            timeAgo = "Just now"
        )

        _userPosts.update { listOf(newPost) + it }
        val toastMsg = "New post published to ${targetPortal.title} under ${accountType.title}! 🎉"
        _latestToast.value = toastMsg
        return newPost
    }

    /**
     * Creates a new market purchase order with the user's chosen transport means,
     * live location permission confirmation, and calculated transport fee.
     * Transport is paid separately after delivery.
     */
    fun createMarketOrder(
        productId: String,
        productTitle: String,
        productPrice: Double,
        productPhotoUrl: String,
        buyerName: String,
        buyerPhone: String,
        buyerAddress: String,
        buyerLat: Double,
        buyerLng: Double,
        sellerName: String,
        transportMeans: TransportMeans,
        paymentMethod: String
    ): MarketDeliveryOrder {
        val order = MarketDeliveryOrder(
            id = "ORD-${(1000..9999).random()}",
            productId = productId,
            productTitle = productTitle,
            productPrice = productPrice,
            productPhotoUrl = productPhotoUrl,
            buyerName = buyerName,
            buyerPhone = buyerPhone,
            buyerLocationAddress = buyerAddress,
            buyerLatitude = buyerLat,
            buyerLongitude = buyerLng,
            sellerName = sellerName,
            transportMeans = transportMeans,
            transportCost = transportMeans.costUsd,
            paymentMethodUsed = paymentMethod,
            status = OrderDeliveryStatus.PENDING_ASSIGNMENT
        )

        _orders.update { listOf(order) + it }

        // Notify the seller to assign a delivery service account of chosen category
        val sellerNotification = AppSystemNotification(
            title = "🛍️ New Order Received!",
            message = "Order #${order.id} for '${order.productTitle}' was purchased by $buyerName. Please assign delivery to a ${transportMeans.title} service account.",
            targetRole = "SELLER",
            relatedOrderId = order.id
        )
        scope.launch {
            _notifications.emit(sellerNotification)
        }
        _latestToast.value = "Order placed! Seller notified to assign ${transportMeans.iconEmoji} ${transportMeans.title} courier."

        return order
    }

    /**
     * Seller assigns a nearby delivery service account of the chosen category
     */
    fun assignDeliveryToServiceAccount(orderId: String, serviceAccount: DeliveryServiceAccount) {
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        status = OrderDeliveryStatus.ASSIGNED_WAITING_PICKUP,
                        assignedServiceAccount = serviceAccount
                    )
                } else order
            }
        }

        // 1. Notify the delivery service account to pick up the delivery
        val serviceNotification = AppSystemNotification(
            title = "📦 Pickup Assignment Alert!",
            message = "You have been assigned to pick up Order #$orderId (${serviceAccount.transportMode.iconEmoji}). Head to the Seller store now.",
            targetRole = "SERVICE_PROVIDER",
            relatedOrderId = orderId
        )

        // 2. Notify the buyer that delivery is assigned
        val buyerNotification = AppSystemNotification(
            title = "🛵 Delivery Assigned!",
            message = "Your delivery has been assigned to ${serviceAccount.name} (${serviceAccount.transportMode.title}).",
            targetRole = "BUYER",
            relatedOrderId = orderId
        )

        scope.launch {
            _notifications.emit(serviceNotification)
            _notifications.emit(buyerNotification)
        }
        _latestToast.value = "Delivery assigned to ${serviceAccount.name}! Service provider & Buyer notified."
    }

    /**
     * Seller verifies pickup by taking a photo of the delivery provider holding the product
     */
    fun sellerVerifyPickupWithPhoto(orderId: String, photoUriOrData: String) {
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        pickupVerificationPhotoUri = photoUriOrData,
                        // If service already accepted or once seller verifies:
                        status = if (order.isPickupAcceptedByService) OrderDeliveryStatus.IN_TRANSIT else OrderDeliveryStatus.PICKED_UP_VERIFIED
                    )
                } else order
            }
        }

        val order = _orders.value.find { it.id == orderId }
        val toastMsg = "Seller submitted pickup proof photo for Order #$orderId! 📸"
        _latestToast.value = toastMsg

        // Check if both seller verified and service accepted
        checkAndNotifyBuyerPickup(orderId)
    }

    /**
     * Delivery service provider verifies pickup by accepting the product
     */
    fun serviceProviderAcceptPickup(orderId: String) {
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        isPickupAcceptedByService = true,
                        status = OrderDeliveryStatus.IN_TRANSIT
                    )
                } else order
            }
        }

        _latestToast.value = "Service provider accepted & confirmed handover! Now navigating with live GPS. 📍"
        checkAndNotifyBuyerPickup(orderId)
    }

    private fun checkAndNotifyBuyerPickup(orderId: String) {
        val order = _orders.value.find { it.id == orderId } ?: return
        // Notify buyer that product has been picked up
        val buyerNotification = AppSystemNotification(
            title = "✅ Package Picked Up & En Route!",
            message = "Your product '${order.productTitle}' has been verified & picked up by ${order.assignedServiceAccount?.name ?: "your courier"}. Live GPS navigation active!",
            targetRole = "BUYER",
            relatedOrderId = orderId
        )
        scope.launch {
            _notifications.emit(buyerNotification)
        }
    }

    /**
     * Complete delivery and collect transport fee (paid separately upon completion)
     */
    fun completeDelivery(orderId: String) {
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    order.copy(status = OrderDeliveryStatus.COMPLETED)
                } else order
            }
        }

        val buyerNotification = AppSystemNotification(
            title = "🎉 Delivery Completed!",
            message = "Order #$orderId handed over successfully. Transport fee settled upon completion.",
            targetRole = "BUYER",
            relatedOrderId = orderId
        )
        scope.launch {
            _notifications.emit(buyerNotification)
        }
        _latestToast.value = "Delivery Completed! Transport fee collected upon handover. ✓"
    }
}
