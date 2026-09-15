package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.portals.PortalCarouselMedia
import com.example.portals.PortalType
import com.example.portals.getPortalCarouselMedia
import com.example.ui.components.CardMediaType
import com.example.ui.components.ProductItem
import com.example.ui.theme.PrimaryBrand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Repository for Room database caching of Portal data, Market items, and Carousel media.
 * Ensures the app remains 100% functional, responsive, and data-rich without active internet.
 */
class PortalRepository(
    private val portalCacheDao: PortalCacheDao,
    private val externalScope: CoroutineScope
) {

    private val _isOfflineCacheReady = MutableStateFlow(false)
    val isOfflineCacheReady: StateFlow<Boolean> = _isOfflineCacheReady.asStateFlow()

    init {
        externalScope.launch(Dispatchers.IO) {
            seedCacheIfEmpty()
            _isOfflineCacheReady.value = true
        }
    }

    /**
     * Reactive stream of cached Market Products persisted in Room.
     */
    val cachedMarketProducts: Flow<List<ProductItem>> = portalCacheDao.getAllMarketProducts()
        .map { entities ->
            entities.map { it.toDomainModel() }
        }

    /**
     * Reactive stream of cached Portal Metadata persisted in Room.
     */
    val cachedPortals: Flow<List<PortalCacheEntity>> = portalCacheDao.getAllPortals()

    /**
     * Reactive stream of cached Carousel Media items grouped by portal type.
     */
    val cachedCarouselMedia: Flow<Map<String, List<PortalCarouselMedia>>> = portalCacheDao.getAllCarouselMedia()
        .map { mediaEntities ->
            mediaEntities.groupBy { it.portalType }
                .mapValues { (_, items) ->
                    items.map { item ->
                        PortalCarouselMedia(
                            title = item.title,
                            subtitle = item.subtitle,
                            imageUrl = item.imageUrl,
                            isVideo = item.isVideo,
                            durationOrTag = item.durationOrTag
                        )
                    }
                }
        }

    /**
     * Seeds initial portal data and market products into Room if the database is currently empty.
     */
    suspend fun seedCacheIfEmpty() = withContext(Dispatchers.IO) {
        val productCount = portalCacheDao.getMarketProductCount()
        if (productCount == 0) {
            val initialProducts = getInitialDefaultProducts().map { it.toEntity() }
            portalCacheDao.insertMarketProducts(initialProducts)
        }

        val portalCount = portalCacheDao.getPortalCacheCount()
        if (portalCount == 0) {
            val initialPortals = listOf(
                PortalCacheEntity(
                    portalKey = "MARKET",
                    title = "Market Shopping Portal",
                    subtitle = "Official Store, Electronics, Fashion & Instant Checkout",
                    badge = "Store Active",
                    themeColorHex = "#10B981",
                    realPhotoUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?auto=format&fit=crop&w=800&q=80"
                ),
                PortalCacheEntity(
                    portalKey = "SERVICE",
                    title = "Service Portal",
                    subtitle = "On-demand expert services, verified tasks & instant booking",
                    badge = "Services Active",
                    themeColorHex = "#34D399",
                    realPhotoUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=800&q=80"
                ),
                PortalCacheEntity(
                    portalKey = "INVESTMENT",
                    title = "Investment & Deposit Portal",
                    subtitle = "Deposit funds, 24/7 on-demand instant investing & yield tracking",
                    badge = "Deposit Ready",
                    themeColorHex = "#00E676",
                    realPhotoUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80"
                ),
                PortalCacheEntity(
                    portalKey = "CUSTOM",
                    title = "Live Web Browser",
                    subtitle = "Browse external web marketplaces & partner tools",
                    badge = "Live Web",
                    themeColorHex = "#6EE7B7",
                    realPhotoUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80"
                )
            )
            portalCacheDao.insertPortals(initialPortals)
        }

        val mediaCount = portalCacheDao.getCarouselMediaCount()
        if (mediaCount == 0) {
            val mediaEntities = mutableListOf<PortalCarouselMediaEntity>()
            listOf(PortalType.MARKET, PortalType.SERVICE, PortalType.INVESTMENT, PortalType.CUSTOM).forEach { portal ->
                val items = getPortalCarouselMedia(portal)
                items.forEachIndexed { index, media ->
                    mediaEntities.add(
                        PortalCarouselMediaEntity(
                            portalType = portal.name,
                            title = media.title,
                            subtitle = media.subtitle,
                            imageUrl = media.imageUrl,
                            isVideo = media.isVideo,
                            durationOrTag = media.durationOrTag,
                            orderIndex = index
                        )
                    )
                }
            }
            portalCacheDao.insertCarouselMedia(mediaEntities)
        }
    }

    /**
     * Decrements product inventory upon local 1-click purchase and updates Room cache.
     */
    suspend fun recordProductPurchase(productId: String) = withContext(Dispatchers.IO) {
        val existing = portalCacheDao.getProductById(productId)
        if (existing != null && existing.stockLeft > 0) {
            portalCacheDao.updateProductStock(productId, existing.stockLeft - 1)
        }
    }

    /**
     * Inserts or caches a new product into the Room database.
     */
    suspend fun saveProduct(product: ProductItem) = withContext(Dispatchers.IO) {
        portalCacheDao.insertProduct(product.toEntity())
    }

    // ==========================================
    // CONVERTERS: Entity <-> Domain
    // ==========================================
    private fun MarketProductEntity.toDomainModel(): ProductItem {
        val specsList = if (specsFormatted.isNotBlank()) {
            specsFormatted.split("||").mapNotNull { pairStr ->
                val parts = pairStr.split(":")
                if (parts.size >= 2) parts[0].trim() to parts.subList(1, parts.size).joinToString(":").trim() else null
            }
        } else emptyList()

        val urls = if (imageUrlsFormatted.isNotBlank()) {
            imageUrlsFormatted.split("||").map { it.trim() }
        } else emptyList()

        val parsedAccent = try {
            val cleanHex = themeAccentHex.removePrefix("#")
            Color(android.graphics.Color.parseColor("#$cleanHex"))
        } catch (_: Exception) {
            PrimaryBrand
        }

        val mediaType = if (defaultMediaType == "IMAGE") CardMediaType.IMAGE else CardMediaType.VIDEO

        return ProductItem(
            id = id,
            title = title,
            category = category,
            sellerAccountName = sellerAccountName,
            sellerAccountHandle = sellerAccountHandle,
            sellerRating = sellerRating,
            sellerSalesCount = sellerSalesCount,
            price = price,
            originalPrice = originalPrice,
            discountPercent = discountPercent,
            stockLeft = stockLeft,
            rating = rating,
            reviewCount = reviewCount,
            description = description,
            specs = specsList,
            shippingTime = shippingTime,
            warrantyText = warrantyText,
            primaryIconEmoji = primaryIconEmoji,
            imageUrls = urls,
            videoPreviewUrl = videoPreviewUrl,
            defaultMediaType = mediaType,
            themeAccent = parsedAccent
        )
    }

    private fun ProductItem.toEntity(): MarketProductEntity {
        val specsStr = specs.joinToString("||") { "${it.first}:${it.second}" }
        val urlsStr = imageUrls.joinToString("||")
        return MarketProductEntity(
            id = id,
            title = title,
            category = category,
            sellerAccountName = sellerAccountName,
            sellerAccountHandle = sellerAccountHandle,
            sellerRating = sellerRating,
            sellerSalesCount = sellerSalesCount,
            price = price,
            originalPrice = originalPrice,
            discountPercent = discountPercent,
            stockLeft = stockLeft,
            rating = rating,
            reviewCount = reviewCount,
            description = description,
            specsFormatted = specsStr,
            shippingTime = shippingTime,
            warrantyText = warrantyText,
            primaryIconEmoji = primaryIconEmoji,
            imageUrlsFormatted = urlsStr,
            videoPreviewUrl = videoPreviewUrl,
            defaultMediaType = defaultMediaType.name,
            themeAccentHex = "#10B981"
        )
    }

    companion object {
        fun getInitialDefaultProducts(): List<ProductItem> {
            return listOf(
                ProductItem(
                    id = "prod_1",
                    title = "Apex Quantum Pro ANC Wireless Spatial Headset",
                    category = "Audio & Sound",
                    sellerAccountName = "Nordic Acoustics Studio",
                    sellerAccountHandle = "@nordic_sound",
                    sellerRating = 4.96f,
                    sellerSalesCount = "12.4k sales",
                    price = 289.99,
                    originalPrice = 399.00,
                    discountPercent = 27,
                    stockLeft = 4,
                    rating = 4.9f,
                    reviewCount = 1840,
                    description = "Studio-grade lossless planar acoustic drivers with ultra-wide 40kHz soundstage, adaptive active noise cancellation, and 60-hour continuous playback.",
                    specs = listOf(
                        "Driver" to "50mm Planar Magnetic",
                        "Battery" to "60h Fast Charge",
                        "Latency" to "15ms Ultra-Low",
                        "Codecs" to "LDAC, aptX Adaptive, AAC"
                    ),
                    primaryIconEmoji = "🎧",
                    imageUrls = listOf(
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1484704849700-f032a568e944?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=1000&q=80"
                    ),
                    videoPreviewUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=1000&q=80",
                    defaultMediaType = CardMediaType.VIDEO,
                    themeAccent = PrimaryBrand
                ),
                ProductItem(
                    id = "prod_2",
                    title = "CyberMatrix Ultra OLED Curved Tactical Monitor (240Hz)",
                    category = "Gaming & Rig",
                    sellerAccountName = "Apex CyberDynamics",
                    sellerAccountHandle = "@cyber_dynamics",
                    sellerRating = 4.92f,
                    sellerSalesCount = "8.1k sales",
                    price = 749.50,
                    originalPrice = 999.00,
                    discountPercent = 25,
                    stockLeft = 3,
                    rating = 4.95f,
                    reviewCount = 920,
                    description = "0.03ms true response time with 240Hz refresh rate, 99.3% DCI-P3 cinematic color spectrum, custom heat sink cooling, and ambient rear bias lighting.",
                    specs = listOf(
                        "Resolution" to "3440 x 1440 UWQHD",
                        "Panel" to "Quantum Dot OLED",
                        "Curve" to "1800R Ergonomic",
                        "Ports" to "HDMI 2.1, DP 1.4, 90W Type-C"
                    ),
                    primaryIconEmoji = "🖥️",
                    imageUrls = listOf(
                        "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1593642702821-c8da6771f0c6?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1547394765-185e1e68f34e?auto=format&fit=crop&w=1000&q=80"
                    ),
                    videoPreviewUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=1000&q=80",
                    defaultMediaType = CardMediaType.VIDEO,
                    themeAccent = PrimaryBrand
                ),
                ProductItem(
                    id = "prod_3",
                    title = "Titan Chronograph Automatic Tourbillon Limited",
                    category = "Luxury Horology",
                    sellerAccountName = "Geneva Haute Atelier",
                    sellerAccountHandle = "@geneva_atelier",
                    sellerRating = 4.99f,
                    sellerSalesCount = "1.2k sales",
                    price = 1450.00,
                    originalPrice = 1850.00,
                    discountPercent = 22,
                    stockLeft = 2,
                    rating = 4.98f,
                    reviewCount = 310,
                    description = "Hand-assembled Swiss mechanical movement with exposed floating tourbillon cage, scratchproof sapphire double crystal, and Grade 5 aerospace titanium casing.",
                    specs = listOf(
                        "Movement" to "Calibre GT-40 Auto",
                        "Case" to "Grade 5 Titanium (41mm)",
                        "Glass" to "Double Sapphire AR",
                        "Waterproof" to "100m / 10 ATM"
                    ),
                    primaryIconEmoji = "⌚",
                    imageUrls = listOf(
                        "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1533139502658-0198f920d8e8?auto=format&fit=crop&w=1000&q=80"
                    ),
                    videoPreviewUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=1000&q=80",
                    defaultMediaType = CardMediaType.IMAGE,
                    themeAccent = PrimaryBrand
                ),
                ProductItem(
                    id = "prod_4",
                    title = "AeroCarbon Pro Mechanical Wireless Keeb (Gasket Mount)",
                    category = "Gaming & Rig",
                    sellerAccountName = "Keycraft Lab Tokyo",
                    sellerAccountHandle = "@keycraft_tokyo",
                    sellerRating = 4.94f,
                    sellerSalesCount = "15.8k sales",
                    price = 195.00,
                    originalPrice = 260.00,
                    discountPercent = 25,
                    stockLeft = 7,
                    rating = 4.93f,
                    reviewCount = 2410,
                    description = "Forged carbon fiber top plate with CNC aluminum base, factory-lubed custom magnetic Hall Effect linear switches, and brass weight sound dampening.",
                    specs = listOf(
                        "Switches" to "Hall Effect Magnetic",
                        "Mount" to "Poron Gasket Dampened",
                        "Battery" to "8000mAh (4 Months)",
                        "Keycaps" to "PBT Double-Shot Dye"
                    ),
                    primaryIconEmoji = "⌨️",
                    imageUrls = listOf(
                        "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1595225476474-87563907a212?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?auto=format&fit=crop&w=1000&q=80"
                    ),
                    videoPreviewUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=1000&q=80",
                    defaultMediaType = CardMediaType.VIDEO,
                    themeAccent = PrimaryBrand
                ),
                ProductItem(
                    id = "prod_5",
                    title = "Lumina Studio 8K Cinema Mirrorless Prime Lens Kit",
                    category = "Smart Studio",
                    sellerAccountName = "CineOptical Systems",
                    sellerAccountHandle = "@cine_optical",
                    sellerRating = 4.97f,
                    sellerSalesCount = "3.4k sales",
                    price = 2199.00,
                    originalPrice = 2800.00,
                    discountPercent = 21,
                    stockLeft = 2,
                    rating = 4.97f,
                    reviewCount = 580,
                    description = "Full-frame 8K ProRes RAW internal video capture with 16+ stops of dynamic range, active cooling turbine, and twin ultra-fast CFexpress Type B slots.",
                    specs = listOf(
                        "Sensor" to "45MP Stacked CMOS",
                        "Video" to "8K/60p ProRes RAW",
                        "Stabilizer" to "8.0 Stops In-Body",
                        "Lens" to "35mm T1.5 Cine Prime"
                    ),
                    primaryIconEmoji = "📷",
                    imageUrls = listOf(
                        "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?auto=format&fit=crop&w=1000&q=80",
                        "https://images.unsplash.com/photo-1512790182412-b19e6d62bc39?auto=format&fit=crop&w=1000&q=80"
                    ),
                    videoPreviewUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=1000&q=80",
                    defaultMediaType = CardMediaType.IMAGE,
                    themeAccent = PrimaryBrand
                )
            )
        }
    }
}
