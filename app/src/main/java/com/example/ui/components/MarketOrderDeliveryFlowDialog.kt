package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import com.example.util.CurrencyUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.AccountOrderDeliveryManager
import com.example.data.DeliveryServiceAccount
import com.example.data.MarketDeliveryOrder
import com.example.data.OrderDeliveryStatus
import com.example.data.TransportMeans
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryYellow
import kotlinx.coroutines.delay

/**
 * Complete Interactive Modal for:
 * 1. Transport Means Selection Grid (Boda Boda, Bicycle, TukTuk, Van, Car, Drone)
 * 2. Live Location Permission & Calculated Transport Fee (Paid Separately After Delivery Note)
 * 3. Wallet Check & Payment Portal (Mobile Money, Airtel Money, Visa Card, Crypto)
 * 4. Seller Order Notification & Assignment to Nearby Suitable Service Account
 * 5. Pickup Verification (Photo of provider holding item + Service acceptance + Buyer notification)
 * 6. Live Location Navigation Route
 */
@Composable
fun MarketOrderDeliveryFlowDialog(
    productId: String,
    productTitle: String,
    productPrice: Double,
    productPhotoUrl: String,
    sellerName: String,
    userWalletBalance: Double,
    deliveryManager: AccountOrderDeliveryManager,
    onWalletDeposit: (Double) -> Unit,
    onOrderFinished: () -> Unit,
    onDismiss: () -> Unit
) {
    // Flow Step:
    // 0: Select Transport Grid
    // 1: Live Location Permission & Transport Cost Review
    // 2: Payment Portal (if insufficient wallet or user chooses direct gateway)
    // 3: Seller Assigns Delivery Service Account
    // 4: Pickup Verification & Service Handover
    // 5: Live GPS Tracking & Handover Completion
    var currentStep by remember { mutableIntStateOf(0) }

    var selectedTransport by remember { mutableStateOf<TransportMeans?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var buyerAddress by remember { mutableStateOf("Plot 14 Nakasero Hill Road, Kampala") }
    var buyerLatitude by remember { mutableDoubleStateOf(0.3176) }
    var buyerLongitude by remember { mutableDoubleStateOf(32.5825) }

    // Payment Portal State
    var selectedPaymentTab by remember { mutableIntStateOf(0) } // 0: Mobile Money, 1: Airtel Money, 2: Visa Card, 3: Crypto
    var mobileMoneyPhone by remember { mutableStateOf("+256 772 458 912") }
    var airtelMoneyPhone by remember { mutableStateOf("+256 752 901 334") }
    var visaCardNumber by remember { mutableStateOf("4242 •••• •••• 8912") }
    var visaExpiry by remember { mutableStateOf("08/28") }
    var visaCvv by remember { mutableStateOf("381") }
    var cryptoSelectedCoin by remember { mutableStateOf("USDT (TRC-20)") }
    var isProcessingPayment by remember { mutableStateOf(false) }
    var activeOrder by remember { mutableStateOf<MarketDeliveryOrder?>(null) }

    // Seller Assignment & Verification
    var selectedServiceProvider by remember { mutableStateOf<DeliveryServiceAccount?>(null) }
    var pickupProofPhoto by remember { mutableStateOf<String?>(null) }
    var isTakingPhoto by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = DarkBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBrand.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Express Delivery & Checkout",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Order: $productTitle • $${String.format("%.2f", productPrice)} (UGX ${CurrencyUtils.formatUgxRaw(productPrice)})",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA0A0B0),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("close_order_dialog_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Step Progress Indicator Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val stepsCount = 6
                    for (i in 0 until stepsCount) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (i <= currentStep) PrimaryBrand else Color(0x33FFFFFF)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content
                Box(modifier = Modifier.weight(1f)) {
                    when (currentStep) {
                        0 -> {
                            // STEP 0: PROMPT OF BUTTONS IN GRID FORM FOR MEANS OF TRANSPORT
                            TransportSelectionGridView(
                                selectedTransport = selectedTransport,
                                onSelect = { transport ->
                                    selectedTransport = transport
                                },
                                onNext = {
                                    if (selectedTransport != null) {
                                        currentStep = 1
                                    }
                                }
                            )
                        }
                        1 -> {
                            // STEP 1: LIVE LOCATION PERMISSION & CALCULATED TRANSPORT COST + SEPARATE TRANSPORT NOTE
                            val transport = selectedTransport ?: TransportMeans.MOTORCYCLE
                            LiveLocationAndCostView(
                                transport = transport,
                                permissionGranted = locationPermissionGranted,
                                buyerAddress = buyerAddress,
                                latitude = buyerLatitude,
                                longitude = buyerLongitude,
                                userWalletBalance = userWalletBalance,
                                productPrice = productPrice,
                                onRequestPermission = {
                                    locationPermissionGranted = true
                                },
                                onProceedToPayment = {
                                    if (userWalletBalance >= productPrice) {
                                        // Wallet has sufficient funds -> create order directly
                                        val newOrder = deliveryManager.createMarketOrder(
                                            productId = productId,
                                            productTitle = productTitle,
                                            productPrice = productPrice,
                                            productPhotoUrl = productPhotoUrl,
                                            buyerName = "Denis Kiiza",
                                            buyerPhone = "+256 772 389 402",
                                            buyerAddress = buyerAddress,
                                            buyerLat = buyerLatitude,
                                            buyerLng = buyerLongitude,
                                            sellerName = sellerName,
                                            transportMeans = transport,
                                            paymentMethod = "Internal Wallet ($${String.format("%.2f", productPrice)} • UGX ${CurrencyUtils.formatUgxRaw(productPrice)})"
                                        )
                                        activeOrder = newOrder
                                        currentStep = 3 // Go to Seller Assignment
                                    } else {
                                        // Insufficient wallet funds -> go to payment portal
                                        currentStep = 2
                                    }
                                }
                            )
                        }
                        2 -> {
                            // STEP 2: PAYMENT PORTAL (Mobile Money, Airtel Money, Visa Card, Crypto)
                            val transport = selectedTransport ?: TransportMeans.MOTORCYCLE
                            PaymentPortalView(
                                productPrice = productPrice,
                                userWalletBalance = userWalletBalance,
                                selectedTab = selectedPaymentTab,
                                onTabChange = { selectedPaymentTab = it },
                                mobileMoneyPhone = mobileMoneyPhone,
                                onMobileMoneyChange = { mobileMoneyPhone = it },
                                airtelMoneyPhone = airtelMoneyPhone,
                                onAirtelMoneyChange = { airtelMoneyPhone = it },
                                visaCardNumber = visaCardNumber,
                                onVisaCardNumberChange = { visaCardNumber = it },
                                visaExpiry = visaExpiry,
                                onVisaExpiryChange = { visaExpiry = it },
                                visaCvv = visaCvv,
                                onVisaCvvChange = { visaCvv = it },
                                cryptoCoin = cryptoSelectedCoin,
                                onCryptoCoinChange = { cryptoSelectedCoin = it },
                                isProcessing = isProcessingPayment,
                                onDepositAndPay = { methodUsed ->
                                    isProcessingPayment = true
                                    // Top up wallet and complete product payment
                                    val depositAmount = (productPrice - userWalletBalance).coerceAtLeast(0.0) + 20.0
                                    onWalletDeposit(depositAmount)

                                    val newOrder = deliveryManager.createMarketOrder(
                                        productId = productId,
                                        productTitle = productTitle,
                                        productPrice = productPrice,
                                        productPhotoUrl = productPhotoUrl,
                                        buyerName = "Denis Kiiza",
                                        buyerPhone = "+256 772 389 402",
                                        buyerAddress = buyerAddress,
                                        buyerLat = buyerLatitude,
                                        buyerLng = buyerLongitude,
                                        sellerName = sellerName,
                                        transportMeans = transport,
                                        paymentMethod = methodUsed
                                    )
                                    activeOrder = newOrder
                                    isProcessingPayment = false
                                    currentStep = 3 // Proceed to Seller Assignment
                                }
                            )
                        }
                        3 -> {
                            // STEP 3: SELLER NOTIFIED TO ASSIGN DELIVERY TO NEARBY SERVICE ACCOUNTS
                            val order = activeOrder
                            val suitableAccounts = deliveryManager.availableServiceAccounts.value.filter {
                                it.transportMode == (order?.transportMeans ?: selectedTransport ?: TransportMeans.MOTORCYCLE)
                            }.ifEmpty { deliveryManager.availableServiceAccounts.value }

                            SellerOrderAssignmentView(
                                order = order,
                                sellerName = sellerName,
                                nearbyAccounts = suitableAccounts,
                                selectedAccount = selectedServiceProvider,
                                onSelectAccount = { selectedServiceProvider = it },
                                onConfirmAssignment = { account ->
                                    if (order != null) {
                                        deliveryManager.assignDeliveryToServiceAccount(order.id, account)
                                        activeOrder = order.copy(
                                            status = OrderDeliveryStatus.ASSIGNED_WAITING_PICKUP,
                                            assignedServiceAccount = account
                                        )
                                        currentStep = 4 // Proceed to Pickup Verification
                                    }
                                }
                            )
                        }
                        4 -> {
                            // STEP 4: PICKUP VERIFICATION (Photo of provider holding item + service provider acceptance + buyer notification)
                            val order = activeOrder
                            PickupVerificationView(
                                order = order,
                                proofPhotoUri = pickupProofPhoto,
                                isTakingPhoto = isTakingPhoto,
                                onTriggerPhotoCapture = {
                                    isTakingPhoto = true
                                },
                                onPhotoCaptured = { simulatedPhotoUri ->
                                    pickupProofPhoto = simulatedPhotoUri
                                    isTakingPhoto = false
                                    if (order != null) {
                                        deliveryManager.sellerVerifyPickupWithPhoto(order.id, simulatedPhotoUri)
                                    }
                                },
                                onServiceProviderAccept = {
                                    if (order != null) {
                                        deliveryManager.serviceProviderAcceptPickup(order.id)
                                        activeOrder = order.copy(
                                            isPickupAcceptedByService = true,
                                            status = OrderDeliveryStatus.IN_TRANSIT
                                        )
                                        currentStep = 5 // Proceed to Live Navigation
                                    }
                                }
                            )
                        }
                        5 -> {
                            // STEP 5: LIVE GPS NAVIGATION & FINAL DELIVERY COMPLETION
                            val order = activeOrder
                            LiveDeliveryTrackingView(
                                order = order,
                                onCompleteHandover = {
                                    if (order != null) {
                                        deliveryManager.completeDelivery(order.id)
                                    }
                                    onOrderFinished()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Step 0: Grid of Transport Means Buttons
 */
@Composable
private fun TransportSelectionGridView(
    selectedTransport: TransportMeans?,
    onSelect: (TransportMeans) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "CHOOSE DELIVERY TRANSPORT",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrand
        )
        Text(
            text = "Select your preferred means of transport for delivery dispatch:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFA0A0B0),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        // 2x3 Grid of Transport Means
        val transportList = TransportMeans.values()
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (rowIndex in 0 until (transportList.size + 1) / 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val first = transportList[rowIndex * 2]
                    val second = transportList.getOrNull(rowIndex * 2 + 1)

                    TransportGridItem(
                        transport = first,
                        isSelected = selectedTransport == first,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelect(first) }
                    )

                    if (second != null) {
                        TransportGridItem(
                            transport = second,
                            isSelected = selectedTransport == second,
                            modifier = Modifier.weight(1f),
                            onClick = { onSelect(second) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmation Button
        Button(
            onClick = onNext,
            enabled = selectedTransport != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("confirm_transport_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBrand,
                contentColor = Color.Black,
                disabledContainerColor = Color(0x331E1E2C),
                disabledContentColor = Color(0x66FFFFFF)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (selectedTransport != null) "Continue with ${selectedTransport.title} →" else "Select a Means of Transport",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TransportGridItem(
    transport: TransportMeans,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryBrand.copy(alpha = 0.18f) else DarkSurface
        ),
        border = null,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("transport_item_${transport.name.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transport.iconEmoji,
                    fontSize = 28.sp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) PrimaryBrand else Color(0x33FFFFFF)
                ) {
                    Text(
                        text = "$${String.format("%.2f", transport.costUsd)} • UGX ${CurrencyUtils.formatUgxRaw(transport.costUsd)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        color = if (isSelected) Color.Black else Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = transport.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "ETA: ${transport.eta}",
                style = MaterialTheme.typography.bodySmall,
                color = MintGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            )

            Text(
                text = transport.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA0A0B0),
                fontSize = 9.sp,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }
    }
}

/**
 * Step 1: Live Location Permission & Calculated Transport Cost View
 * Explicitly notes: Transport is paid separately after delivery completion.
 */
@Composable
private fun LiveLocationAndCostView(
    transport: TransportMeans,
    permissionGranted: Boolean,
    buyerAddress: String,
    latitude: Double,
    longitude: Double,
    userWalletBalance: Double,
    productPrice: Double,
    onRequestPermission: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "LIVE LOCATION & TRANSPORT COST",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrand
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Selected Transport Summary Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = transport.iconEmoji, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transport.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Estimated Arrival: ${transport.eta}",
                        color = MintGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Est. Cost (USD/UGX)",
                        color = Color(0xFFA0A0B0),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "$${String.format("%.2f", transport.costUsd)}",
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBrand,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "UGX ${CurrencyUtils.formatUgxRaw(transport.costUsd)}",
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live Location Permission Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (permissionGranted) Icons.Default.CheckCircle else Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = if (permissionGranted) EmeraldSuccess else PrimaryBrand,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (permissionGranted) "Live Location Shared ✓" else "Share Live GPS Location",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }

                    if (!permissionGranted) {
                        Button(
                            onClick = onRequestPermission,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBrand,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("grant_live_location_button")
                        ) {
                            Text("Allow GPS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (permissionGranted)
                        "Live GPS active: $buyerAddress (Lat: ${String.format("%.4f", latitude)}, Lng: ${String.format("%.4f", longitude)})"
                    else
                        "The courier needs your live location to navigate directly to your coordinates in real-time.",
                    color = Color(0xFFC0C0D0),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // MANDATORY NOTE: Transport fee paid separately after delivery
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = PrimaryYellow.copy(alpha = 0.12f),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = PrimaryYellow,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "IMPORTANT TRANSPORT PAYMENT NOTE",
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryYellow,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "The transport cost ($${String.format("%.2f", transport.costUsd)} • UGX ${CurrencyUtils.formatUgxRaw(transport.costUsd)}) is paid separately ONLY after the delivery is completed and safely handed over to you.",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Product Checkout Balance Summary
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Product Cost (Due Now):", color = Color(0xFFA0A0B0), fontSize = 12.sp)
                    Text(
                        text = "$${String.format("%.2f", productPrice)} • UGX ${CurrencyUtils.formatUgxRaw(productPrice)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Account Wallet Balance:", color = Color(0xFFA0A0B0), fontSize = 12.sp)
                    Text(
                        text = "$${String.format("%.2f", userWalletBalance)} • UGX ${CurrencyUtils.formatUgxRaw(userWalletBalance)}",
                        color = if (userWalletBalance >= productPrice) EmeraldSuccess else AccentRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                if (userWalletBalance < productPrice) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Insufficient funds in wallet! You will be guided to the Payment Portal (Mobile Money, Airtel, Visa, Crypto).",
                        color = AccentRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        Button(
            onClick = onProceedToPayment,
            enabled = permissionGranted,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("proceed_from_location_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (userWalletBalance >= productPrice) EmeraldSuccess else PrimaryBrand,
                contentColor = Color.Black,
                disabledContainerColor = Color(0x331E1E2C),
                disabledContentColor = Color(0x66FFFFFF)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (!permissionGranted) "Please Share Live Location First"
                else if (userWalletBalance >= productPrice) "Pay with Wallet ($${String.format("%.2f", productPrice)} • UGX ${CurrencyUtils.formatUgxRaw(productPrice)}) & Place Order →"
                else "Open Payment Portal & Deposit ($${String.format("%.2f", productPrice)} • UGX ${CurrencyUtils.formatUgxRaw(productPrice)}) →",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Step 2: Payment Portal (Mobile Money, Airtel Money, Visa Card, Crypto)
 */
@Composable
private fun PaymentPortalView(
    productPrice: Double,
    userWalletBalance: Double,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    mobileMoneyPhone: String,
    onMobileMoneyChange: (String) -> Unit,
    airtelMoneyPhone: String,
    onAirtelMoneyChange: (String) -> Unit,
    visaCardNumber: String,
    onVisaCardNumberChange: (String) -> Unit,
    visaExpiry: String,
    onVisaExpiryChange: (String) -> Unit,
    visaCvv: String,
    onVisaCvvChange: (String) -> Unit,
    cryptoCoin: String,
    onCryptoCoinChange: (String) -> Unit,
    isProcessing: Boolean,
    onDepositAndPay: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "DEPOSIT & PAYMENT PORTAL",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrand
        )
        Text(
            text = "Complete product payment of $${String.format("%.2f", productPrice)} • UGX ${CurrencyUtils.formatUgxRaw(productPrice)} using your preferred provider:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFA0A0B0),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )

        // 4 Payment Gateway Tabs
        val paymentTabs = listOf("📱 MTN Momo", "🔴 Airtel", "💳 Visa", "🪙 Crypto")
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = PrimaryBrand,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
        ) {
            paymentTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabChange(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            color = if (selectedTab == index) PrimaryBrand else Color(0xFFA0A0B0)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gateway Form Fields
        when (selectedTab) {
            0 -> {
                // Mobile Money (MTN / M-Pesa)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("MTN Mobile Money / M-Pesa", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    OutlinedTextField(
                        value = mobileMoneyPhone,
                        onValueChange = onMobileMoneyChange,
                        label = { Text("Mobile Money Phone Number") },
                        leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = PrimaryBrand) },
                        modifier = Modifier.fillMaxWidth().testTag("momo_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBrand,
                            unfocusedBorderColor = DarkCardBorder
                        )
                    )
                    Text("A 4-digit push prompt will appear on your phone to confirm debit.", color = Color(0xFFA0A0B0), fontSize = 10.sp)
                }
            }
            1 -> {
                // Airtel Money
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Airtel Money Express", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    OutlinedTextField(
                        value = airtelMoneyPhone,
                        onValueChange = onAirtelMoneyChange,
                        label = { Text("Airtel Number (+256...)") },
                        leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = AccentRed) },
                        modifier = Modifier.fillMaxWidth().testTag("airtel_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = DarkCardBorder
                        )
                    )
                    Text("Instant USSD pin prompt dispatched to your SIM card.", color = Color(0xFFA0A0B0), fontSize = 10.sp)
                }
            }
            2 -> {
                // Visa Card
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Visa / Mastercard Global Gateway", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    OutlinedTextField(
                        value = visaCardNumber,
                        onValueChange = onVisaCardNumberChange,
                        label = { Text("Card Number") },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = PrimaryBrand) },
                        modifier = Modifier.fillMaxWidth().testTag("visa_card_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBrand,
                            unfocusedBorderColor = DarkCardBorder
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = visaExpiry,
                            onValueChange = onVisaExpiryChange,
                            label = { Text("Expiry (MM/YY)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBrand,
                                unfocusedBorderColor = DarkCardBorder
                            )
                        )
                        OutlinedTextField(
                            value = visaCvv,
                            onValueChange = onVisaCvvChange,
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBrand,
                                unfocusedBorderColor = DarkCardBorder
                            )
                        )
                    }
                }
            }
            3 -> {
                // Crypto
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cryptocurrency Web3 Pay (Instant Settlement)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("USDT (TRC-20)", "BTC", "ETH").forEach { coin ->
                            val isChosen = cryptoCoin == coin
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isChosen) PrimaryBrand else DarkSurface,
                                border = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onCryptoCoinChange(coin) }
                            ) {
                                Text(
                                    text = coin,
                                    color = if (isChosen) Color.Black else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurface,
                        border = null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Deposit Address ($cryptoCoin)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("TKj9xV72mQ5PaaW891rX2zL12kXopL89", color = Color(0xFFA0A0B0), fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Deposit & Pay Button
        Button(
            onClick = {
                val method = when (selectedTab) {
                    0 -> "MTN Mobile Money ($mobileMoneyPhone)"
                    1 -> "Airtel Money ($airtelMoneyPhone)"
                    2 -> "Visa Card ending in ${visaCardNumber.takeLast(4)}"
                    else -> "Crypto $cryptoCoin"
                }
                onDepositAndPay(method)
            },
            enabled = !isProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("submit_deposit_pay_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldSuccess,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
            } else {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Deposit & Pay $${String.format("%.2f", productPrice)} (UGX ${CurrencyUtils.formatUgxRaw(productPrice)}) Now",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Step 3: Seller Notified & Shows Available Suitable Accounts Near Location
 */
@Composable
private fun SellerOrderAssignmentView(
    order: MarketDeliveryOrder?,
    sellerName: String,
    nearbyAccounts: List<DeliveryServiceAccount>,
    selectedAccount: DeliveryServiceAccount?,
    onSelectAccount: (DeliveryServiceAccount) -> Unit,
    onConfirmAssignment: (DeliveryServiceAccount) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Notification Alert Box
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = EmeraldSuccess.copy(alpha = 0.15f),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "🔔 SELLER NOTIFICATION TRIGGERED",
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Owner of '$sellerName' received notification: Assign Order #${order?.id ?: "ORD"} to a ${order?.transportMeans?.title ?: "courier"} delivery service account.",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AVAILABLE SUITABLE ACCOUNTS NEAR LOCATION",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrand
        )
        Text(
            text = "Showing vetted delivery service accounts matching ${order?.transportMeans?.iconEmoji ?: "🛵"} ${order?.transportMeans?.title ?: "Couriers"} closest to seller store:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFA0A0B0),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )

        // List of nearby accounts
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            nearbyAccounts.forEach { account ->
                val isSelected = selectedAccount?.id == account.id
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryBrand.copy(alpha = 0.16f) else DarkSurface
                    ),
                    border = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectAccount(account) }
                        .testTag("service_account_card_${account.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = account.avatarUrl,
                            contentDescription = account.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = account.name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "⭐ ${account.rating}",
                                    color = PrimaryYellow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${account.transportMode.iconEmoji} ${account.transportMode.title} • Plate: ${account.vehicleNumberPlate}",
                                color = Color(0xFFA0A0B0),
                                fontSize = 11.sp
                            )
                            Text(
                                text = "📍 ${account.distanceKm} km away • ${account.completedDeliveries} completed",
                                color = MintGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) PrimaryBrand else Color(0x22FFFFFF),
                            modifier = Modifier.size(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedAccount != null) {
                    onConfirmAssignment(selectedAccount)
                }
            },
            enabled = selectedAccount != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("confirm_service_assignment_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBrand,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (selectedAccount != null) "Assign Delivery to ${selectedAccount.name} →" else "Select a Nearby Service Account",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Step 4: Pickup Verification
 * - Selling account takes picture of delivery provider holding product & submits
 * - Delivery service provider verifies pickup by accepting product
 * - Buyer receives notification that product has been picked up
 */
@Composable
private fun PickupVerificationView(
    order: MarketDeliveryOrder?,
    proofPhotoUri: String?,
    isTakingPhoto: Boolean,
    onTriggerPhotoCapture: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    onServiceProviderAccept: () -> Unit
) {
    val assignedService = order?.assignedServiceAccount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Dual Notification Header
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = PrimaryBrand.copy(alpha = 0.15f),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "NOTIFICATIONS DISPATCHED",
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBrand,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "1. Service Account (${assignedService?.name ?: "Courier"}) notified to pick up.\n2. Buyer notified that delivery has been assigned!",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "PICKUP PROOF & HANDOVER VERIFICATION",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrand
        )
        Text(
            text = "Two-step verification guarantees zero package loss and full chain of custody:",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFA0A0B0),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        // STEP 4A: Seller Photo Verification
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. Seller: Take Photo of Courier Holding Item",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    if (proofPhotoUri != null) {
                        Surface(shape = RoundedCornerShape(6.dp), color = EmeraldSuccess.copy(alpha = 0.2f)) {
                            Text("Verified ✓", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (proofPhotoUri != null) {
                    // Preview captured photo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = proofPhotoUri,
                            contentDescription = "Handover Photo Proof",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xCC000000),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "📸 Handover Proof Submitted • ${assignedService?.vehicleNumberPlate ?: "Courier"}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                } else {
                    // Button to trigger photo capture
                    Button(
                        onClick = {
                            // Capture photo of courier holding item
                            val sampleProofPhoto = "https://images.unsplash.com/photo-1580674684081-7617fbf3d745?auto=format&fit=crop&w=600&q=80"
                            onPhotoCaptured(sampleProofPhoto)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBrand,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("capture_pickup_photo_button")
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Camera & Capture Courier Holding Item", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // STEP 4B: Service Account Accepts Handover
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "2. Service Account: Accept & Confirm Handover",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = "The courier (${assignedService?.name ?: "Service Provider"}) verifies package sealed integrity and accepts handover.",
                    color = Color(0xFFA0A0B0),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                Button(
                    onClick = onServiceProviderAccept,
                    enabled = proofPhotoUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldSuccess,
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0x331E1E2C),
                        disabledContentColor = Color(0x66FFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("service_accept_handover_button")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (proofPhotoUri == null) "Awaiting Seller Photo Proof First" else "Confirm Handover & Start Live GPS Navigation →",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Step 5: Live Delivery Tracking with Live Buyer Coordinates
 * - Buyer notified that package has been picked up
 * - Delivery provider navigates using buyer's live coordinates
 * - Final step: complete delivery & collect transport fee (paid separately)
 */
@Composable
private fun LiveDeliveryTrackingView(
    order: MarketDeliveryOrder?,
    onCompleteHandover: () -> Unit
) {
    val assigned = order?.assignedServiceAccount
    var simulatedDistanceMeters by remember { mutableIntStateOf(1450) }
    var simulatedSpeedKmh by remember { mutableIntStateOf(34) }

    LaunchedEffect(Unit) {
        while (simulatedDistanceMeters > 50) {
            delay(1500)
            simulatedDistanceMeters = (simulatedDistanceMeters - 180).coerceAtLeast(0)
            simulatedSpeedKmh = (30..42).random()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Buyer Notified Confirmation Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = EmeraldSuccess.copy(alpha = 0.15f),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "BUYER NOTIFIED: PACKAGE PICKED UP! 🎉",
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "The buyer was notified that ${assigned?.name ?: "courier"} has picked up the order and is en route with live GPS coordinates.",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Live Simulated Navigation Radar & Route Map
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFF0F172A))
                ) {
                    // Custom Canvas simulated map grid & route
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        // Grid lines
                        for (x in 0..canvasWidth.toInt() step 60) {
                            drawLine(
                                color = Color(0x1A64748B),
                                start = Offset(x.toFloat(), 0f),
                                end = Offset(x.toFloat(), canvasHeight),
                                strokeWidth = 1f
                            )
                        }
                        for (y in 0..canvasHeight.toInt() step 60) {
                            drawLine(
                                color = Color(0x1A64748B),
                                start = Offset(0f, y.toFloat()),
                                end = Offset(canvasWidth, y.toFloat()),
                                strokeWidth = 1f
                            )
                        }

                        // Route path
                        val start = Offset(canvasWidth * 0.15f, canvasHeight * 0.75f)
                        val end = Offset(canvasWidth * 0.85f, canvasHeight * 0.25f)
                        drawLine(
                            color = PrimaryBrand,
                            start = start,
                            end = end,
                            strokeWidth = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                        )

                        // Courier moving indicator
                        val progress = 1f - (simulatedDistanceMeters / 1450f).coerceIn(0f, 1f)
                        val currentPos = Offset(
                            start.x + (end.x - start.x) * progress,
                            start.y + (end.y - start.y) * progress
                        )

                        // Destination circle
                        drawCircle(
                            color = EmeraldSuccess,
                            radius = 12f,
                            center = end
                        )

                        // Courier pulse
                        drawCircle(
                            color = PrimaryBrand.copy(alpha = 0.35f),
                            radius = 24f,
                            center = currentPos
                        )
                        drawCircle(
                            color = PrimaryBrand,
                            radius = 10f,
                            center = currentPos
                        )
                    }

                    // Floating telemetry HUD
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xCC0B0F19),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE GPS: ${simulatedSpeedKmh} km/h • ${simulatedDistanceMeters}m away",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xCC0B0F19),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Destination: ${order?.buyerLocationAddress ?: "Kampala"}",
                            color = EmeraldSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Delivery Info
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = assigned?.transportMode?.iconEmoji ?: "🛵", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = assigned?.name ?: "Assigned Courier",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Vehicle Plate: ${assigned?.vehicleNumberPlate ?: "UFA 889K"}",
                                    color = Color(0xFFA0A0B0),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Surface(shape = RoundedCornerShape(8.dp), color = MintGreen.copy(alpha = 0.2f)) {
                            Text(
                                text = if (simulatedDistanceMeters <= 50) "Arrived! 📍" else "In Transit 🚀",
                                color = MintGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Transport Fee Reminder
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PrimaryBrand.copy(alpha = 0.1f),
            border = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "TRANSPORT SETTLEMENT AT HANDOVER",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBrand,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Transport fee of $${String.format("%.2f", order?.transportCost ?: 2.50)} (UGX ${CurrencyUtils.formatUgxRaw(order?.transportCost ?: 2.50)}) is settled separately upon package handover.",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Complete Handover Button
        Button(
            onClick = onCompleteHandover,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("complete_handover_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldSuccess,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Confirm Handover & Settle Transport Fee ✓",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
