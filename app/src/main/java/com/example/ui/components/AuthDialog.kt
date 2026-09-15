package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AccountOrderDeliveryManager
import com.example.data.AuthUser
import com.example.data.MarketDeliveryOrder
import com.example.data.UserAccountType
import com.example.portals.PortalType
import com.example.ui.theme.AccentRed
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryYellow
import java.util.Locale

@Composable
fun AuthDialog(
    currentUser: AuthUser,
    onDismiss: () -> Unit,
    onLogin: (email: String, name: String) -> Unit,
    onLogout: () -> Unit,
    onSwitchAccountType: ((UserAccountType) -> Unit)? = null,
    onCreatePost: ((title: String, desc: String, price: Double, label: String, category: String, photo: String) -> Unit)? = null,
    onOpenPortal: ((PortalType) -> Unit)? = null,
    deliveryOrders: List<MarketDeliveryOrder> = emptyList(),
    deliveryManager: AccountOrderDeliveryManager? = null
) {
    var selectedTab by remember { mutableIntStateOf(if (currentUser.isLoggedIn) 2 else 0) } // 0: Sign In, 1: Register, 2: Profile/Demo
    var emailInput by remember { mutableStateOf(if (currentUser.isLoggedIn) currentUser.email else "kiizadenis13@gmail.com") }
    var passwordInput by remember { mutableStateOf("••••••••") }
    var nameInput by remember { mutableStateOf(currentUser.name) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var useBiometric by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successToast by remember { mutableStateOf<String?>(null) }
    var showCreatePostDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = null,
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .testTag("auth_dialog_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryBrand.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (currentUser.isLoggedIn) Icons.Default.AccountCircle else Icons.Default.Login,
                                contentDescription = null,
                                tint = PrimaryBrand,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (currentUser.isLoggedIn) "Account Profile" else "Sign In & Log In",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (currentUser.isLoggedIn) "Authenticated Session" else "Access your real-time command suite",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("auth_dialog_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: Sign In / Log In vs Register vs Profile
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = PrimaryBrand
                            )
                        }
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0; errorMessage = null },
                        text = {
                            Text(
                                "Sign In",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) PrimaryBrand else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.testTag("auth_tab_signin")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1; errorMessage = null },
                        text = {
                            Text(
                                "Register",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) PrimaryBrand else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.testTag("auth_tab_register")
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2; errorMessage = null },
                        text = {
                            Text(
                                if (currentUser.isLoggedIn) "Active Profile" else "Demo 1-Click",
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) PrimaryBrand else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.testTag("auth_tab_demo")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content
                when (selectedTab) {
                    0 -> { // Sign In / Log In Form
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it; errorMessage = null },
                                label = { Text("Email or Username", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(18.dp))
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_email_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it; errorMessage = null },
                                label = { Text("Password", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(18.dp))
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_password_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = rememberMe,
                                        onCheckedChange = { rememberMe = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PrimaryBrand),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Remember me", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { useBiometric = !useBiometric }
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = null,
                                        tint = if (useBiometric) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Biometrics",
                                        fontSize = 11.sp,
                                        color = if (useBiometric) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (errorMessage != null) {
                                Text(
                                    text = errorMessage!!,
                                    color = AccentRed,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = {
                                    if (emailInput.isBlank()) {
                                        errorMessage = "Please enter an email or username"
                                    } else {
                                        val displayName = if (emailInput.contains("@")) emailInput.substringBefore("@").replace(".", " ").capitalizeWords() else emailInput
                                        onLogin(emailInput, displayName)
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("auth_submit_login_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBrand,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Log In to Account", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    1 -> { // Register Tab
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = { nameInput = it; errorMessage = null },
                                label = { Text("Full Name", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(18.dp))
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_register_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it; errorMessage = null },
                                label = { Text("Email Address", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(18.dp))
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_register_email_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it; errorMessage = null },
                                label = { Text("Create Master Password", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(18.dp))
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBrand,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            if (errorMessage != null) {
                                Text(
                                    text = errorMessage!!,
                                    color = AccentRed,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = {
                                    if (nameInput.isBlank() || emailInput.isBlank()) {
                                        errorMessage = "Please enter your name and email"
                                    } else {
                                        onLogin(emailInput, nameInput)
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("auth_submit_register_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldSuccess,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Create & Sign In", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    2 -> { // Profile / Quick 1-Click Demo Profiles
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (currentUser.isLoggedIn) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = PrimaryBrand.copy(alpha = 0.12f),
                                    border = null,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(Color(currentUser.avatarColorHex).copy(alpha = 0.3f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (currentUser.avatarPhotoUrl.isNotBlank()) {
                                                AsyncImage(
                                                    model = currentUser.avatarPhotoUrl,
                                                    contentDescription = currentUser.name,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                                                )
                                            } else {
                                                Text(text = currentUser.avatarEmoji, fontSize = 22.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = currentUser.name,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = EmeraldSuccess.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        text = "Active",
                                                        color = EmeraldSuccess,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = currentUser.email,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = "Tier: ${currentUser.accountType}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = PrimaryYellow,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }

                                // ---------------------------------------------
                                // ACCOUNT TYPE SWITCHER (Service, Sellers, Founders)
                                // ---------------------------------------------
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "SELECT ACTIVE ACCOUNT TYPE",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBrand,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = "Switch role to post offerings to its designated portal:",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )

                                    UserAccountType.values().forEach { accountType ->
                                        val isSelected = currentUser.activeAccountType == accountType
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) PrimaryBrand.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                            border = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    onSwitchAccountType?.invoke(accountType)
                                                }
                                                .testTag("account_type_switch_${accountType.name.lowercase()}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = accountType.iconEmoji, fontSize = 24.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = accountType.title,
                                                            fontWeight = FontWeight.Bold,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = if (isSelected) PrimaryBrand else MaterialTheme.colorScheme.onSurface
                                                        )
                                                        if (isSelected) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Surface(
                                                                shape = RoundedCornerShape(4.dp),
                                                                color = PrimaryBrand
                                                            ) {
                                                                Text(
                                                                    text = "ACTIVE",
                                                                    color = Color.Black,
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                    Text(
                                                        text = "Posts feed to: ${accountType.targetPortalName}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = EmeraldSuccess,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 10.sp
                                                    )
                                                    Text(
                                                        text = accountType.description,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 10.sp,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Quick Actions for Active Account: Create Post & View Portal
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { showCreatePostDialog = true },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .testTag("create_post_button_account"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryBrand,
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Create Post", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            onOpenPortal?.invoke(currentUser.activeAccountType.defaultPortal)
                                            onDismiss()
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .testTag("open_active_portal_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        border = null
                                    ) {
                                        Icon(Icons.Default.OpenInNew, contentDescription = null, tint = PrimaryBrand, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Open Portal", color = PrimaryBrand, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }

                                // Wallet Balance Box
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = null,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(22.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Wallet Balance (USD & UGX)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text(
                                                    text = "$${String.format("%.2f", currentUser.walletBalance)} • UGX ${com.example.util.CurrencyUtils.formatUgxRaw(currentUser.walletBalance)}",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = EmeraldSuccess
                                                )
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = EmeraldSuccess.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = "Mobile Money / Card / Crypto Ready",
                                                color = EmeraldSuccess,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        onLogout()
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .testTag("auth_logout_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentRed.copy(alpha = 0.18f),
                                        contentColor = AccentRed
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sign Out of Session", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 2.dp))
                                Text("Or Switch Demo Profile:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            // 1-Click Fast Profiles
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DemoUserButton(
                                    emoji = "👨‍💻",
                                    photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80",
                                    name = "Denis Kiiza",
                                    role = "Lead Admin",
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        onLogin("kiizadenis13@gmail.com", "Denis Kiiza")
                                        onDismiss()
                                    }
                                )
                                DemoUserButton(
                                    emoji = "👩‍💼",
                                    photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80",
                                    name = "Sarah Jenkins",
                                    role = "Lead Partner",
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        onLogin("sarah.j@meridian-ventures.com", "Sarah Jenkins")
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Create Post Dialog
        if (showCreatePostDialog) {
            CreatePostDialog(
                accountType = currentUser.activeAccountType,
                authorName = currentUser.name,
                onDismiss = { showCreatePostDialog = false },
                onSubmit = { title, desc, price, label, cat, photo ->
                    onCreatePost?.invoke(title, desc, price, label, cat, photo)
                    showCreatePostDialog = false
                }
            )
        }
    }
}

@Composable
private fun DemoUserButton(
    emoji: String,
    photoUrl: String? = null,
    name: String,
    role: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = null,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (photoUrl != null) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                )
            } else {
                Text(text = emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = role, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, maxLines = 1)
        }
    }
}

private fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() } }
}
