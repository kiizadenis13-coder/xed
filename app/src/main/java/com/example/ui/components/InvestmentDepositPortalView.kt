package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.util.CurrencyUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.UserAccountPost
import com.example.portals.PortalType
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryBrandDark
import java.text.NumberFormat
import java.util.Locale

data class InvestmentVault(
    val id: String,
    val name: String,
    val annualYieldPercent: Double,
    val riskLevel: String,
    val description: String,
    val iconEmoji: String,
    val photoUrl: String,
    val minDeposit: Double = 10.0,
    val termLabel: String = "Flexible (Withdraw Anytime)"
)

data class UserPosition(
    val id: String,
    val vaultName: String,
    val initialDeposit: Double,
    var currentBalance: Double,
    val dailyEarnings: Double,
    val dateStarted: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InvestmentDepositPortalView(
    onClose: () -> Unit,
    userPosts: List<UserAccountPost> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Incorporate offerings published from Founders Accounts
    val founderPostsAsVaults = remember(userPosts) {
        userPosts.filter { it.targetPortal == PortalType.INVEST }.map { post ->
            InvestmentVault(
                id = post.id,
                name = "${post.title} (by ${post.authorName})",
                annualYieldPercent = post.priceOrRate,
                riskLevel = "Founder Verified Equity / Yield",
                description = post.description,
                iconEmoji = "🚀",
                photoUrl = if (post.photoUrl.isNotBlank()) post.photoUrl else "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=800&q=80",
                minDeposit = 50.0,
                termLabel = post.priceLabel
            )
        }
    }

    var userCashBalance by remember { mutableDoubleStateOf(5240.00) }
    var totalInvestedBalance by remember { mutableDoubleStateOf(12850.00) }
    var depositAmountText by remember { mutableStateOf("100") }
    var selectedTab by remember { mutableStateOf(0) } // 0: Vaults, 1: Quick Deposit, 2: Calculator
    var successNotification by remember { mutableStateOf<String?>(null) }
    var calculatorDeposit by remember { mutableFloatStateOf(500f) }

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
        }
    }

    val baseVaults = remember {
        listOf(
            InvestmentVault(
                id = "vault_emerald",
                name = "Emerald High-Yield Cash Vault",
                annualYieldPercent = 5.6,
                riskLevel = "Guaranteed Zero Risk",
                description = "FDIC-insured daily interest accrual. Instant deposits and withdrawals anytime.",
                iconEmoji = "🏦",
                photoUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80"
            ),
            InvestmentVault(
                id = "vault_ai",
                name = "Apex AI & Tech Innovation Fund",
                annualYieldPercent = 14.2,
                riskLevel = "High Growth / Moderate",
                description = "Curated institutional shares in robotics, next-gen cloud infra, and machine learning.",
                iconEmoji = "🤖",
                photoUrl = "https://images.unsplash.com/photo-1526304640581-d334cdbbf45e?auto=format&fit=crop&w=800&q=80"
            ),
            InvestmentVault(
                id = "vault_green",
                name = "Sustainable Clean Energy Grid",
                annualYieldPercent = 9.4,
                riskLevel = "Eco-Friendly / Steady",
                description = "Direct dividends from solar arrays, battery storage plants, and green power grids.",
                iconEmoji = "🌱",
                photoUrl = "https://images.unsplash.com/photo-1497435334941-8c899ee9e8e9?auto=format&fit=crop&w=800&q=80"
            ),
            InvestmentVault(
                id = "vault_treasury",
                name = "Federal Short-Term Treasury Reserve",
                annualYieldPercent = 4.9,
                riskLevel = "Ultra-Safe Government",
                description = "US Treasury backed daily yield tokens. Maximum security for long-term reserves.",
                iconEmoji = "🏛️",
                photoUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80"
            )
        )
    }

    val availableVaults = remember(founderPostsAsVaults, baseVaults) {
        founderPostsAsVaults + baseVaults
    }

    val userPositions = remember {
        mutableStateListOf(
            UserPosition(
                id = "pos_1",
                vaultName = "Emerald High-Yield Cash Vault",
                initialDeposit = 5000.0,
                currentBalance = 5280.0,
                dailyEarnings = 0.78,
                dateStarted = "Active 42 days"
            ),
            UserPosition(
                id = "pos_2",
                vaultName = "Apex AI & Tech Innovation Fund",
                initialDeposit = 7000.0,
                currentBalance = 7570.0,
                dailyEarnings = 2.94,
                dateStarted = "Active 28 days"
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PitchBlack)
            .testTag("investment_deposit_portal_view")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Bar
            item {
                Surface(
                    color = DarkSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBrand.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Investment & Deposit Portal",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Deposit funds & watch your money grow 🌱",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MintGreen
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DarkElevated)
                                .testTag("close_investment_portal_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Portal",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Notification Banner
            item {
                AnimatedVisibility(
                    visible = successNotification != null,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut()
                ) {
                    successNotification?.let { msg ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryBrand.copy(alpha = 0.2f),
                            border = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Hero Balance Cards
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Total Invested Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = null,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = PrimaryBrand,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "GROWING WEALTH",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MintGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currencyFormatter.format(totalInvestedBalance),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PrimaryBrand
                                )
                                Text(
                                    text = "≈ UGX ${CurrencyUtils.formatUgxRaw(totalInvestedBalance)}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintGreen
                                )
                                Text(
                                    text = "+$3.72 today 🚀",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        // Liquid Cash Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = null,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Savings,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD54F),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "WALLET CASH",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFFFD54F),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currencyFormatter.format(userCashBalance),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "≈ UGX ${CurrencyUtils.formatUgxRaw(userCashBalance)}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD54F)
                                )
                                Text(
                                    text = "Ready to deposit",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Quick Nav Pills
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("🌱 Investment Vaults", "⚡ 1-Tap Deposit", "🧮 Growth Calculator").forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) PrimaryBrand else DarkSurface,
                            border = null,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            // Tab 0: Investment Vaults
            if (selectedTab == 0) {
                item {
                    Text(
                        text = "EXPLORE INVESTMENT VAULTS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MintGreen,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }

                items(availableVaults) { vault ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = null,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("vault_card_${vault.id}")
                    ) {
                        Column {
                            // High Res Real Photography Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                AsyncImage(
                                    model = vault.photoUrl,
                                    contentDescription = vault.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color(0x33000000),
                                                    Color(0xCC000000)
                                                )
                                            )
                                        )
                                )
                                // Yield Badge
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = PrimaryBrand,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "+${vault.annualYieldPercent}% APY",
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "${vault.iconEmoji} ${vault.name}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                )
                            }

                            // Details & Action
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = vault.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD0D0D8),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = PrimaryBrand,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = vault.riskLevel,
                                            fontSize = 11.sp,
                                            color = MintGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            if (userCashBalance >= 100.0) {
                                                userCashBalance -= 100.0
                                                totalInvestedBalance += 100.0
                                                userPositions.add(
                                                    UserPosition(
                                                        id = "pos_${System.currentTimeMillis()}",
                                                        vaultName = vault.name,
                                                        initialDeposit = 100.0,
                                                        currentBalance = 100.0,
                                                        dailyEarnings = (100.0 * vault.annualYieldPercent / 100.0) / 365.0,
                                                        dateStarted = "Started today"
                                                    )
                                                )
                                                successNotification = "Deposited $100.00 into ${vault.name}! 🎉"
                                            } else {
                                                successNotification = "Need more wallet cash! Use the 1-Tap Deposit tab below."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryBrand,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .height(38.dp)
                                            .testTag("invest_button_${vault.id}")
                                    ) {
                                        Text("invest", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tab 1: 1-Tap Quick Deposit
            if (selectedTab == 1) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = null,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBolt,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "1-Tap Instant Wallet Deposit",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "Add money to your savings or investment balance with 1 simple tap.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick Amount Pills
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(10, 25, 50, 100, 250, 500).forEach { amount ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (depositAmountText == amount.toString()) PrimaryBrand else DarkElevated,
                                        border = null,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { depositAmountText = amount.toString() }
                                    ) {
                                        Text(
                                            text = "+$$amount",
                                            color = if (depositAmountText == amount.toString()) Color.Black else Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = depositAmountText,
                                onValueChange = { depositAmountText = it.filter { ch -> ch.isDigit() } },
                                label = { Text("Custom Amount ($)") },
                                leadingIcon = {
                                    Text("$", color = PrimaryBrand, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val amount = depositAmountText.toDoubleOrNull() ?: 100.0
                                    if (amount > 0) {
                                        userCashBalance += amount
                                        successNotification = "Successfully deposited ${currencyFormatter.format(amount)} to Wallet! 🎉"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBrand,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_deposit_button")
                            ) {
                                Text(
                                    text = "Confirm Instant Deposit 🚀",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // Tab 2: Kid-Friendly Interactive Growth Calculator
            if (selectedTab == 2) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = null,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoGraph,
                                    contentDescription = null,
                                    tint = PrimaryBrand,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Interactive Savings Growth Calculator",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "See how much your money multiplies over time with compound growth!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Monthly Savings: ${currencyFormatter.format(calculatorDeposit.toDouble())}",
                                color = PrimaryBrand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Slider(
                                value = calculatorDeposit,
                                onValueChange = { calculatorDeposit = it },
                                valueRange = 25f..2500f,
                                steps = 24,
                                colors = SliderDefaults.colors(
                                    thumbColor = PrimaryBrand,
                                    activeTrackColor = PrimaryBrand,
                                    inactiveTrackColor = DarkElevated
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Results Showcase (1 yr, 3 yrs, 5 yrs)
                            val rate = 0.08
                            val val1Year = calculatorDeposit * 12 * (1 + rate)
                            val val3Years = calculatorDeposit * 36 * (1 + rate * 3)
                            val val5Years = calculatorDeposit * 60 * (1 + rate * 5.8)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "In 1 Year" to val1Year,
                                    "In 3 Years" to val3Years,
                                    "In 5 Years" to val5Years
                                ).forEach { (time, value) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DarkElevated,
                                        border = null,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = time, fontSize = 10.sp, color = MintGreen, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currencyFormatter.format(value),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // User's Active Investment Positions
            item {
                Text(
                    text = "YOUR ACTIVE POSITIONS (${userPositions.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MintGreen,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(userPositions) { position ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = null,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = position.vaultName,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "${position.dateStarted} • +${currencyFormatter.format(position.dailyEarnings)}/day",
                                fontSize = 11.sp,
                                color = MintGreen
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = currencyFormatter.format(position.currentBalance),
                                fontWeight = FontWeight.Black,
                                color = PrimaryBrand,
                                fontSize = 15.sp
                            )
                            Button(
                                onClick = {
                                    userCashBalance += position.currentBalance
                                    totalInvestedBalance -= position.currentBalance
                                    userPositions.remove(position)
                                    successNotification = "Withdrew ${currencyFormatter.format(position.currentBalance)} to wallet cash! 💵"
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkElevated,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Withdraw", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
