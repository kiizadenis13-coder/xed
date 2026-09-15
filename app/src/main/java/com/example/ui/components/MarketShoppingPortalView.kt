package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.data.AccountOrderDeliveryManager
import com.example.data.MarketDeliveryOrder
import com.example.data.UserAccountPost
import com.example.portals.PortalType
import com.example.util.CurrencyUtils
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryBrandDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Account User Media Preference: Video Reel vs Photo Gallery
 */
enum class CardMediaType {
    VIDEO,
    IMAGE
}

data class ProductItem(
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
    val specs: List<Pair<String, String>>,
    val shippingTime: String = "Free 1-Day Air Delivery",
    val warrantyText: String = "2-Year Manufacturer Warranty",
    val primaryIconEmoji: String = "⚡",
    val imageUrls: List<String> = emptyList(),
    val videoPreviewUrl: String? = null,
    val defaultMediaType: CardMediaType = CardMediaType.VIDEO,
    val themeAccent: Color = PrimaryBrand
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MarketShoppingPortalView(
    onClose: () -> Unit,
    cachedProducts: List<ProductItem>? = null,
    onProductPurchased: ((String) -> Unit)? = null,
    isRoomCached: Boolean = true,
    userPosts: List<UserAccountPost> = emptyList(),
    walletBalance: Double = 3450.00,
    deliveryManager: AccountOrderDeliveryManager? = null,
    onTopUpWallet: ((Double) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Incorporate posts published from Sellers Accounts
    val sellerPostsAsProducts = remember(userPosts) {
        userPosts.filter { it.targetPortal == PortalType.MARKET }.map { post ->
            ProductItem(
                id = post.id,
                title = post.title,
                category = post.category,
                sellerAccountName = post.authorName,
                sellerAccountHandle = "@${post.authorName.lowercase().replace(" ", "")}",
                sellerRating = 4.95f,
                sellerSalesCount = "480+ sold",
                price = post.priceOrRate,
                originalPrice = post.priceOrRate * 1.25,
                discountPercent = 20,
                stockLeft = 15,
                rating = 4.9f,
                reviewCount = 58,
                description = post.description,
                specs = listOf("Seller" to post.authorName, "Category" to post.category, "Delivery" to "Express Couriers"),
                shippingTime = "Same-Day Courier Express",
                warrantyText = "1-Year Merchant Guarantee",
                primaryIconEmoji = "🛍️",
                imageUrls = listOf(if (post.photoUrl.isNotBlank()) post.photoUrl else "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80"),
                videoPreviewUrl = "https://assets.mixkit.co/videos/preview/mixkit-young-man-shopping-online-on-laptop-42903-large.mp4",
                defaultMediaType = CardMediaType.IMAGE,
                themeAccent = PrimaryBrand
            )
        }
    }

    val baseProductsList = remember(cachedProducts) {
        if (!cachedProducts.isNullOrEmpty()) cachedProducts else sampleMarketProducts
    }

    val activeProductsList = remember(sellerPostsAsProducts, baseProductsList) {
        sellerPostsAsProducts + baseProductsList
    }

    val allCategories = remember {
        listOf(
            "All Products",
            "Audio & Sound",
            "Gaming & Rig",
            "Luxury Horology",
            "Electronics",
            "Smart Studio"
        )
    }

    var selectedCategory by remember { mutableStateOf("All Products") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Active product delivery/order checkout dialog
    var activeCheckoutProduct by remember { mutableStateOf<ProductItem?>(null) }

    // User shopping wallet & cart state to display interactive gradient odometer counters
    var shoppingBalance by remember(walletBalance) { mutableDoubleStateOf(walletBalance) }
    var totalCartItems by remember { mutableIntStateOf(0) }
    val likedProductIds = remember { mutableStateMapOf<String, Boolean>() }
    val purchasedProductIds = remember { mutableStateMapOf<String, Boolean>() }
    // Per-card media type preference map (Video vs Image)
    val mediaTypePreferences = remember(activeProductsList) {
        mutableStateMapOf<String, CardMediaType>().apply {
            activeProductsList.forEach { put(it.id, it.defaultMediaType) }
        }
    }

    val filteredProducts = remember(selectedCategory, searchQuery, activeProductsList) {
        activeProductsList.filter { product ->
            val matchesCategory = selectedCategory == "All Products" ||
                    product.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    product.title.contains(searchQuery, ignoreCase = true) ||
                    product.sellerAccountName.contains(searchQuery, ignoreCase = true) ||
                    product.sellerAccountHandle.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val infinitePageCount = if (filteredProducts.isNotEmpty()) 100_000 else 1
    val initialPage = if (filteredProducts.isNotEmpty()) 50_000 - (50_000 % filteredProducts.size) else 0
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { infinitePageCount })
    val coroutineScope = rememberCoroutineScope()

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
        }
    }

    // Root Container: Card takes 100% of the entire screen
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07070A))
            .testTag("market_shopping_portal_view")
    ) {
        // Vertical Pager: FULL SCREEN CARD TAKES 100% OF VIEWPORT IN CONTINUOUS INFINITE LOOP
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products found for '$searchQuery'",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try another category or clear your search keyword.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA0A0AA)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            searchQuery = ""
                            selectedCategory = "All Products"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrand, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Show All Products", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("market_vertical_pager")
            ) { page ->
                val actualIndex = page % filteredProducts.size
                val product = filteredProducts[actualIndex]
                val isLiked = likedProductIds[product.id] == true
                val isPurchased = purchasedProductIds[product.id] == true
                val mediaType = mediaTypePreferences[product.id] ?: product.defaultMediaType

                FullScreenProductMediaCard(
                    product = product,
                    mediaType = mediaType,
                    pageIndex = actualIndex,
                    totalPages = filteredProducts.size,
                    isLiked = isLiked,
                    isPurchased = isPurchased,
                    onToggleMediaType = {
                        val nextType = if (mediaType == CardMediaType.VIDEO) CardMediaType.IMAGE else CardMediaType.VIDEO
                        mediaTypePreferences[product.id] = nextType
                    },
                    onToggleLike = {
                        likedProductIds[product.id] = !isLiked
                    },
                    onBuyNow = {
                        activeCheckoutProduct = product
                    },
                    onScrollNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
            }
        }

        // Active Order Delivery Workflow (6-step modal with Transport, Location, Payment, Nearby Services & Tracking)
        if (activeCheckoutProduct != null) {
            val checkoutProduct = activeCheckoutProduct!!
            val effectiveDeliveryManager = deliveryManager ?: remember { AccountOrderDeliveryManager(coroutineScope) }
            MarketOrderDeliveryFlowDialog(
                productId = checkoutProduct.id,
                productTitle = checkoutProduct.title,
                productPrice = checkoutProduct.price,
                productPhotoUrl = checkoutProduct.imageUrls.firstOrNull() ?: "",
                sellerName = checkoutProduct.sellerAccountName,
                userWalletBalance = shoppingBalance,
                deliveryManager = effectiveDeliveryManager,
                onWalletDeposit = { added ->
                    shoppingBalance += added
                    onTopUpWallet?.invoke(added)
                },
                onOrderFinished = {
                    purchasedProductIds[checkoutProduct.id] = true
                    shoppingBalance = (shoppingBalance - checkoutProduct.price).coerceAtLeast(0.0)
                    totalCartItems += 1
                    onProductPurchased?.invoke(checkoutProduct.id)
                    activeCheckoutProduct = null
                },
                onDismiss = { activeCheckoutProduct = null }
            )
        }

        // ==========================================
        // FLOATING TRANSLUCENT TOP BUTTONS & HEADER
        // Floats directly above the full-screen card
        // Semi-transparent so the card media is visible through it
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC08080E),
                            Color(0x8808080E),
                            Color(0x3308080E),
                            Color.Transparent
                        )
                    )
                )
                .statusBarsPadding()
                .padding(bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Action & Title Bar: Frosted translucent glass badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Close Button (Translucent circular glass)
                    Surface(
                        shape = CircleShape,
                        color = Color(0x66181824),
                        border = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onClose() }
                            .testTag("market_portal_close_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Market",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Center: Translucent Brand & Live Wallet Balance Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x66141420),
                        border = null,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBrand)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MARKET",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 1.sp,
                                fontSize = 11.sp
                            )
                            if (isRoomCached) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldSuccess.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "ROOM CACHE",
                                        color = EmeraldSuccess,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(12.dp)
                                    .background(Color(0x44FFFFFF))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bal: $${String.format("%.0f", shoppingBalance)} • UGX ${CurrencyUtils.formatUgxRaw(shoppingBalance)}",
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Right: Cart Counter Pill (Translucent)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (totalCartItems > 0) PrimaryBrand.copy(alpha = 0.25f) else Color(0x66181824),
                        border = null,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping Cart",
                                tint = if (totalCartItems > 0) PrimaryBrand else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            OdometerText(
                                text = "$totalCartItems",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                digitKind = DigitKind.COUNT,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Expandable Search Bar (Translucent glass)
                AnimatedVisibility(
                    visible = isSearchExpanded,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search products, accounts, hardware...", fontSize = 11.sp, color = Color.LightGray) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("market_search_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color(0xAA141420),
                                unfocusedContainerColor = Color(0x88141420),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                // Horizontal Translucent Category Filter Chips + FLOATING SEARCH BUTTON
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(top = 2.dp)
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(allCategories) { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) PrimaryBrand else Color(0x55181826),
                                border = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedCategory = category }
                                    .testTag("market_category_${category.lowercase().replace(" ", "_")}")
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.Black else Color(0xFFECECF4),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Floating Translucent Search Action Button
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSearchExpanded || searchQuery.isNotEmpty()) PrimaryBrand else Color(0x66181826),
                            border = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { isSearchExpanded = !isSearchExpanded }
                                .testTag("market_floating_search_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Search Market",
                                    tint = if (isSearchExpanded || searchQuery.isNotEmpty()) Color.Black else Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * FULL SCREEN IMMERSIVE PRODUCT CARD
 * Takes 100% of the screen. Supports dynamic Video Reel and Photo Gallery modes.
 * Words are neatly organized in a bottom scrim and right HUD rail so the image/video remains crystal clear!
 */
@Composable
private fun FullScreenProductMediaCard(
    product: ProductItem,
    mediaType: CardMediaType,
    pageIndex: Int,
    totalPages: Int,
    isLiked: Boolean,
    isPurchased: Boolean,
    onToggleMediaType: () -> Unit,
    onToggleLike: () -> Unit,
    onBuyNow: () -> Unit,
    onScrollNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var activeImageIndex by remember { mutableIntStateOf(0) }
    var showSpecsModal by remember { mutableStateOf(false) }

    // Dynamic Video playback simulation progress (0f to 1f)
    var videoProgress by remember { mutableFloatStateOf(0.18f) }
    var videoCurrentSeconds by remember { mutableIntStateOf(6) }

    LaunchedEffect(isPlaying, mediaType) {
        if (mediaType == CardMediaType.VIDEO && isPlaying) {
            while (true) {
                delay(1000)
                videoCurrentSeconds = (videoCurrentSeconds + 1) % 30
                videoProgress = videoCurrentSeconds / 30f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .testTag("product_card_${product.id}")
    ) {
        // ==========================================
        // 1. FULL-SCREEN BACKGROUND MEDIA CANVAS (Video or Image)
        // Takes the whole screen from top to bottom
        // ==========================================
        if (mediaType == CardMediaType.VIDEO) {
            FullScreenProductVideoCanvas(
                product = product,
                isPlaying = isPlaying,
                isMuted = isMuted,
                videoProgress = videoProgress,
                onTogglePlayPause = { isPlaying = !isPlaying }
            )
        } else {
            FullScreenProductImageCanvas(
                product = product,
                activeImageIndex = activeImageIndex,
                onCycleImage = { activeImageIndex = (activeImageIndex + 1) % 3 }
            )
        }

        // ==========================================
        // 2. RIGHT-SIDE FLOATING HUD ACTION RAIL
        // Translucent glass action buttons neatly stacked
        // ==========================================
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Media Mode Switcher (Video vs Image toggle)
            TranslucentRailButton(
                icon = if (mediaType == CardMediaType.VIDEO) Icons.Default.Videocam else Icons.Default.Collections,
                label = if (mediaType == CardMediaType.VIDEO) "VIDEO" else "PHOTO",
                isActive = true,
                activeColor = PrimaryBrand,
                onClick = onToggleMediaType,
                testTag = "media_toggle_${product.id}"
            )

            // Like / Wishlist Button
            TranslucentRailButton(
                icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                label = if (isLiked) "SAVED" else "SAVE",
                isActive = isLiked,
                activeColor = AccentRed,
                onClick = onToggleLike,
                testTag = "like_product_${product.id}"
            )

            // Full Specs Modal Button
            TranslucentRailButton(
                icon = Icons.Default.Info,
                label = "SPECS",
                isActive = false,
                onClick = { showSpecsModal = true },
                testTag = "specs_product_${product.id}"
            )

            // Scroll to next product button in infinite loop
            if (totalPages > 1) {
                TranslucentRailButton(
                    icon = Icons.Default.KeyboardArrowDown,
                    label = "NEXT",
                    isActive = false,
                    onClick = onScrollNext,
                    testTag = "next_product_${product.id}"
                )
            }
        }

        // ==========================================
        // 3. BOTTOM SCRIM & NEATLY ORGANIZED TEXT HUD
        // Gradient overlay ensures high contrast and clarity
        // while leaving the middle media area completely clear!
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x6607070C),
                            Color(0xDD07070C),
                            Color(0xF907070C)
                        )
                    )
                )
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Seller Account Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Seller Avatar
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(product.themeAccent, AccentRed)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = product.sellerAccountName.take(2).uppercase(),
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.sellerAccountName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Merchant",
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.sellerAccountHandle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA2A2B0),
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "★ ${product.sellerRating} (${product.sellerSalesCount})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PrimaryBrand,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Media Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x66000000),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (mediaType == CardMediaType.VIDEO) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isPlaying) EmeraldSuccess else AccentRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "4K REEL • 00:${videoCurrentSeconds.toString().padStart(2, '0')}/00:30",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Collections,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PHOTO ${activeImageIndex + 1}/3",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Product Title (High contrast display text)
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Pricing Row: Precision Emerald Gradient Odometer + Discount Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess
                    )
                    OdometerText(
                        text = "%.2f".format(product.price),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        digitKind = DigitKind.MONEY,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EmeraldSuccess.copy(alpha = 0.18f),
                        border = null
                    ) {
                        Text(
                            text = "UGX ${CurrencyUtils.formatUgxRaw(product.price)}",
                            color = EmeraldSuccess,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$%.2f".format(product.originalPrice),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888898),
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AccentRed
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Shipping badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x66000000),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Free Air 1-Day",
                                color = EmeraldSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Feature pills summary (Non-obstructive)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.specs.take(3).forEach { (k, v) ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0x551B1B26),
                            border = null
                        ) {
                            Text(
                                text = "$k: $v",
                                color = Color(0xFFDCDCE8),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Bar: Specs on Left + BUY Button on the Right Bottom Corner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick Specs Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x66181826),
                        border = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showSpecsModal = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "View Specs",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Item Specs",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Button on the right bottom corner of each card posted called BUY
                    Button(
                        onClick = onBuyNow,
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("buy_product_button_${product.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPurchased) EmeraldSuccess else PrimaryBrand,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isPurchased) Icons.Default.CheckCircle else Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPurchased) "PURCHASED ✓" else "BUY",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 4. DETAILED SPECIFICATIONS MODAL BOTTOM SHEET
        // ==========================================
        if (showSpecsModal) {
            ProductSpecsSheet(
                product = product,
                onDismiss = { showSpecsModal = false }
            )
        }
    }
}

/**
 * FULL-SCREEN VIDEO CANVAS SIMULATION
 * Features animated loop lighting, audio visualizer, timeline progress, and video HUD.
 */
@Composable
private fun FullScreenProductVideoCanvas(
    product: ProductItem,
    isPlaying: Boolean,
    isMuted: Boolean,
    videoProgress: Float,
    onTogglePlayPause: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "video_motion")
    val ambientSweep by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambient_sweep"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onTogglePlayPause() },
        contentAlignment = Alignment.Center
    ) {
        // Dynamic Studio Lighting Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Center glow following simulated studio lights
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        product.themeAccent.copy(alpha = 0.22f),
                        Color(0x181820).copy(alpha = 0.5f),
                        Color(0xFF0A0A10)
                    ),
                    center = Offset(w * 0.5f, h * 0.45f),
                    radius = w * 0.75f
                )
            )

            // Animated subtle tech grid scan lines
            val scanY = (h * (videoProgress % 1f))
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, product.themeAccent.copy(alpha = 0.4f), Color.Transparent)
                ),
                start = Offset(0f, scanY),
                end = Offset(w, scanY),
                strokeWidth = 2f
            )
        }

        // Main Center Video Hero Presentation Takes Over Whole Screen
        val videoPoster = product.videoPreviewUrl ?: product.imageUrls.firstOrNull()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (videoPoster != null) {
                AsyncImage(
                    model = videoPoster,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Cinematic vignette overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                )
            }

            // Center stickers made invisible so the video view is unobstructed
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = PrimaryBrand,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Video Progress Timeline Indicator
        LinearProgressIndicator(
            progress = { videoProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 88.dp),
            color = PrimaryBrand,
            trackColor = Color(0x33FFFFFF)
        )
    }
}

/**
 * FULL-SCREEN PHOTO GALLERY CANVAS WITH REAL PHOTOGRAPHY
 * Features authentic product photo studio lighting, macro views, and photo cycle indicators.
 */
@Composable
private fun FullScreenProductImageCanvas(
    product: ProductItem,
    activeImageIndex: Int,
    onCycleImage: () -> Unit
) {
    val photoAngles = listOf("Front Angle", "Macro Detail", "Studio Setup")
    val currentImageUrl = if (product.imageUrls.isNotEmpty()) {
        product.imageUrls[activeImageIndex % product.imageUrls.size]
    } else {
        null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCycleImage() },
        contentAlignment = Alignment.Center
    ) {
        // Real Life Photography Canvas Takes Over Whole Screen
        if (currentImageUrl != null) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = currentImageUrl,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.15f),
                                    Color.Black.copy(alpha = 0.60f)
                                )
                            )
                        )
                )
            }
        } else {
            // Studio Photography Backdrop Canvas Fallback
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF142820),
                            Color(0xFF0D1E16),
                            Color(0xFF060E0A)
                        ),
                        center = Offset(w * 0.5f, h * 0.42f),
                        radius = w * 0.85f
                    )
                )
            }
        }

        // Photo Angle Pill
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xDD000000),
            border = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(top = 96.dp, start = 14.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Collections,
                    contentDescription = null,
                    tint = PrimaryBrand,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val angleLabel = photoAngles.getOrElse(activeImageIndex) { "Angle ${activeImageIndex + 1}" }
                Text(
                    text = "$angleLabel • Tap to Cycle (${activeImageIndex + 1}/${product.imageUrls.size.coerceAtLeast(1)})",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Translucent Floating Action Rail Button
 */
@Composable
fun TranslucentRailButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    activeColor: Color = PrimaryBrand,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) activeColor.copy(alpha = 0.25f) else Color(0x66181826),
            border = null,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .testTag(testTag)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isActive) activeColor else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) activeColor else Color(0xFFD0D0E0),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Detailed Specifications Sheet Modal
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProductSpecsSheet(
    product: ProductItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF14141E)),
            border = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Specifications & Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD4D4E0),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "HARDWARE SPECS",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryBrand,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.specs.forEach { (k, v) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E1E2C),
                            border = null
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                                Text(text = k, color = Color(0xFFA0A0B0), fontSize = 9.sp)
                                Text(text = v, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Buyer Protection
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1A1A24),
                    border = null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100% Authentic Guaranteed • 2-Year Manufacturer Warranty",
                            color = Color(0xFFC0C0D0),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

private val sampleMarketProducts = listOf(
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
        themeAccent = EmeraldSuccess
    ),
    ProductItem(
        id = "prod_3",
        title = "Velox Chrono Apex Titanium Smartwatch (Solar Saphire)",
        category = "Luxury Horology",
        sellerAccountName = "Luxe Horizon Horology",
        sellerAccountHandle = "@luxe_horology",
        sellerRating = 4.98f,
        sellerSalesCount = "4.2k sales",
        price = 580.00,
        originalPrice = 720.00,
        discountPercent = 19,
        stockLeft = 7,
        rating = 4.88f,
        reviewCount = 612,
        description = "Aerospace grade Grade-5 titanium chassis with dual-frequency GPS, biometric continuous ECG telemetry, offline topological maps, and infinite solar battery charging.",
        specs = listOf(
            "Material" to "Titanium + Sapphire Glass",
            "Water Rating" to "100m / 10 ATM",
            "Sensors" to "ECG, SpO2, Skin Temp, GPS",
            "Battery" to "30 Days Solar Boosted"
        ),
        primaryIconEmoji = "⌚",
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1546868871-7041f2a55e12?auto=format&fit=crop&w=1000&q=80"
        ),
        videoPreviewUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=1000&q=80",
        defaultMediaType = CardMediaType.IMAGE,
        themeAccent = PrimaryBrand
    ),
    ProductItem(
        id = "prod_4",
        title = "NeuralCore AI Pro Accelerator Desktop Dock",
        category = "Electronics",
        sellerAccountName = "Quantum Visionics",
        sellerAccountHandle = "@quantum_vision",
        sellerRating = 4.91f,
        sellerSalesCount = "5.6k sales",
        price = 450.00,
        originalPrice = 599.00,
        discountPercent = 24,
        stockLeft = 6,
        rating = 4.91f,
        reviewCount = 430,
        description = "Dedicated on-premise NPU tensor processing unit with 64 TOPS compute, dual Thunderbolt 4 ports, and seamless one-click local LLM inference hosting.",
        specs = listOf(
            "Compute" to "64 TOPS Neural Engine",
            "Interface" to "Thunderbolt 4 / USB4",
            "Cooling" to "Vapor Chamber Silent",
            "Power" to "140W GaN Pass-Through"
        ),
        primaryIconEmoji = "⚡",
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?auto=format&fit=crop&w=1000&q=80"
        ),
        videoPreviewUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=1000&q=80",
        defaultMediaType = CardMediaType.VIDEO,
        themeAccent = AccentGreen
    ),
    ProductItem(
        id = "prod_5",
        title = "Aura Studio Smart Ambient Luminaire (Matter Compatible)",
        category = "Smart Studio",
        sellerAccountName = "Urban Matrix Studio",
        sellerAccountHandle = "@urban_matrix",
        sellerRating = 4.89f,
        sellerSalesCount = "19.3k sales",
        price = 149.99,
        originalPrice = 199.00,
        discountPercent = 25,
        stockLeft = 11,
        rating = 4.82f,
        reviewCount = 2150,
        description = "Precision biophilic circadian rhythm lighting with 16 million customizable gradients, music sync radar sensor, and zero-latency Matter over Thread connectivity.",
        specs = listOf(
            "Lumens" to "2,200 lm Dynamic",
            "Protocol" to "Matter / Thread / Zigbee",
            "Color Temp" to "1,800K - 6,500K Tunable",
            "Build" to "Anodized Matte Aluminum"
        ),
        primaryIconEmoji = "💡",
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1540932239986-30128078f3c5?auto=format&fit=crop&w=1000&q=80"
        ),
        videoPreviewUrl = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=1000&q=80",
        defaultMediaType = CardMediaType.IMAGE,
        themeAccent = MintGreen
    ),
    ProductItem(
        id = "prod_6",
        title = "Apex Tactical Mechanical Split Ergonomic Keyboard",
        category = "Electronics",
        sellerAccountName = "Astro Nova Hardware",
        sellerAccountHandle = "@astro_hardware",
        sellerRating = 4.97f,
        sellerSalesCount = "3.8k sales",
        price = 219.00,
        originalPrice = 280.00,
        discountPercent = 22,
        stockLeft = 5,
        rating = 4.94f,
        reviewCount = 890,
        description = "Hot-swappable magnetic Hall effect linear switches with rapid trigger 0.1mm actuation, wireless low-latency Bluetooth 5.3, and sculpted PBT keycaps.",
        specs = listOf(
            "Switches" to "Magnetic Hall Effect Linear",
            "Polling Rate" to "8,000Hz Ultra-Response",
            "Layout" to "Columnar Split Ergonomic",
            "Battery" to "300h Wireless"
        ),
        primaryIconEmoji = "⌨️",
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1595225476474-87563907a212?auto=format&fit=crop&w=1000&q=80"
        ),
        videoPreviewUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=1000&q=80",
        defaultMediaType = CardMediaType.VIDEO,
        themeAccent = PrimaryBrand
    )
)
