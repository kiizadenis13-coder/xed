package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.UserAccountType
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryBrand
import com.example.util.CurrencyUtils

/**
 * Modal to create a post under the user's active account:
 * - Service Account -> Streamed to Service Portal
 * - Sellers Account -> Streamed to Market Portal
 * - Founders Account -> Streamed to Invest Portal
 */
@Composable
fun CreatePostDialog(
    accountType: UserAccountType,
    authorName: String,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        description: String,
        priceOrRate: Double,
        priceLabel: String,
        category: String,
        photoUrl: String
    ) -> Unit
) {
    var title by remember {
        mutableStateOf(
            when (accountType) {
                UserAccountType.SERVICE_ACCOUNT -> "Full-Stack Software & Architecture Consultation"
                UserAccountType.SELLER_ACCOUNT -> "UltraPro Wireless Smart Earbuds ANC"
                UserAccountType.FOUNDER_ACCOUNT -> "AfriClean Energy Expansion Vault"
            }
        )
    }
    var category by remember {
        mutableStateOf(
            when (accountType) {
                UserAccountType.SERVICE_ACCOUNT -> "Tech & Software"
                UserAccountType.SELLER_ACCOUNT -> "Electronics"
                UserAccountType.FOUNDER_ACCOUNT -> "CleanTech Seed"
            }
        )
    }
    var priceOrRateText by remember {
        mutableStateOf(
            when (accountType) {
                UserAccountType.SERVICE_ACCOUNT -> "65.0"
                UserAccountType.SELLER_ACCOUNT -> "149.0"
                UserAccountType.FOUNDER_ACCOUNT -> "22.5"
            }
        )
    }
    var description by remember {
        mutableStateOf(
            when (accountType) {
                UserAccountType.SERVICE_ACCOUNT -> "On-demand system design, API development, and mobile architectural consulting."
                UserAccountType.SELLER_ACCOUNT -> "High fidelity noise-cancelling wireless earbuds with 40-hour playtime and wireless charging."
                UserAccountType.FOUNDER_ACCOUNT -> "Seed investment round to deploy 50 off-grid solar micro-grids with tokenized yield."
            }
        )
    }
    var photoUrl by remember {
        mutableStateOf(accountType.defaultPortal.realPhotoUrl)
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .testTag("create_post_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
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
                                Icon(Icons.Default.Add, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Create New Listing",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Posting as ${accountType.badge}",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryBrand,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Portal destination banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PrimaryBrand.copy(alpha = 0.12f),
                    border = null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Destination Portal: ",
                            fontSize = 11.sp,
                            color = Color(0xFFA0A0B0)
                        )
                        Text(
                            text = "${accountType.iconEmoji} ${accountType.defaultPortal.title}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBrand
                        )
                    }
                }

                // Form Fields
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = { Text("Title / Offering Name") },
                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null, tint = PrimaryBrand) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("post_title_input"),
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
                        value = category,
                        onValueChange = { category = it; errorMessage = null },
                        label = { Text("Category") },
                        leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = PrimaryBrand) },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("post_category_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBrand,
                            unfocusedBorderColor = DarkCardBorder
                        )
                    )

                    OutlinedTextField(
                        value = priceOrRateText,
                        onValueChange = { priceOrRateText = it; errorMessage = null },
                        label = {
                            Text(
                                when (accountType) {
                                    UserAccountType.SERVICE_ACCOUNT -> "Rate ($/hr)"
                                    UserAccountType.SELLER_ACCOUNT -> "Price ($)"
                                    UserAccountType.FOUNDER_ACCOUNT -> "APY (%)"
                                }
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = EmeraldSuccess) },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("post_price_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldSuccess,
                            unfocusedBorderColor = DarkCardBorder
                        )
                    )
                }

                // Live Dual Currency (USD and UGX) conversion feedback
                val enteredNum = priceOrRateText.toDoubleOrNull()
                if (enteredNum != null && enteredNum > 0.0 && accountType != UserAccountType.FOUNDER_ACCOUNT) {
                    Text(
                        text = "≈ UGX ${CurrencyUtils.formatUgxRaw(enteredNum)} • Rate: 1 USD = 3,750 UGX",
                        fontSize = 11.sp,
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it; errorMessage = null },
                    label = { Text("Description & Deliverables") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = PrimaryBrand) },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth().testTag("post_description_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBrand,
                        unfocusedBorderColor = DarkCardBorder
                    )
                )

                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it; errorMessage = null },
                    label = { Text("Photo URL (Optional)") },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = PrimaryBrand) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("post_photo_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBrand,
                        unfocusedBorderColor = DarkCardBorder
                    )
                )

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        val priceNum = priceOrRateText.toDoubleOrNull()
                        if (title.isBlank()) {
                            errorMessage = "Please enter a title."
                        } else if (priceNum == null || priceNum <= 0.0) {
                            errorMessage = "Please enter a valid price/rate."
                        } else {
                            val label = when (accountType) {
                                UserAccountType.SERVICE_ACCOUNT -> "$${String.format("%.0f", priceNum)}/hr • UGX ${CurrencyUtils.formatUgxRaw(priceNum)}/hr"
                                UserAccountType.SELLER_ACCOUNT -> "$${String.format("%.2f", priceNum)} • UGX ${CurrencyUtils.formatUgxRaw(priceNum)}"
                                UserAccountType.FOUNDER_ACCOUNT -> "+${String.format("%.1f", priceNum)}% APY"
                            }
                            onSubmit(title, description, priceNum, label, category, photoUrl)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("publish_post_submit_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBrand,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Publish to ${accountType.defaultPortal.title}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
