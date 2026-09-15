package com.example.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.GoalCategory
import com.example.data.GoalEntity
import com.example.data.LiveActivityEvent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.window.Dialog
import com.example.portals.getPortalCarouselMedia
import com.example.portals.PortalCarouselMedia
import com.example.portals.PortalType
import com.example.ui.components.AddGoalDialog
import com.example.ui.components.AppShowcaseVideoSection
import com.example.ui.components.AuthDialog
import com.example.ui.components.ChatConversationDialog
import com.example.ui.components.DigitKind
import com.example.ui.components.HomeContactsHub
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.MinimizedKpiBar
import com.example.ui.components.OdometerText
import com.example.ui.components.RealtimeSparklineChart
import com.example.ui.components.SameScreenSidebar
import com.example.ui.components.UserProgressOverview
import com.example.ui.components.WebPortalModal
import com.example.ui.components.XportLogo
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkYellow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryYellow
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val analyticsState by viewModel.analyticsState.collectAsStateWithLifecycle()
    val goals by viewModel.goals.collectAsStateWithLifecycle()
    val selectedTimeRange by viewModel.selectedTimeRange.collectAsStateWithLifecycle()
    val isSidebarExpanded by viewModel.isSidebarExpanded.collectAsStateWithLifecycle()
    val activeModalPortal by viewModel.activeModalPortal.collectAsStateWithLifecycle()
    val showAddGoalDialog by viewModel.showAddGoalDialog.collectAsStateWithLifecycle()

    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val messagesMap by viewModel.messagesMap.collectAsStateWithLifecycle()
    val hasPhonePermission by viewModel.hasPhonePermission.collectAsStateWithLifecycle()
    val activeChatContact by viewModel.activeChatContact.collectAsStateWithLifecycle()
    val cachedMarketProducts by viewModel.cachedMarketProducts.collectAsStateWithLifecycle()
    val isRoomCacheReady by viewModel.isRoomCacheReady.collectAsStateWithLifecycle()
    val userPosts by viewModel.userPosts.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.requestPhoneContactsSync()
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            viewModel.requestPhoneContactsSync()
        } else {
            contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    val totalGoals = goals.size
    val completedGoals = goals.count { it.isCompleted }
    val overallCompletionRate = if (totalGoals > 0) completedGoals.toFloat() / totalGoals.toFloat() else 0.72f

    val investmentProgress = goals.filter { it.category == GoalCategory.INVESTMENT }
        .let { list -> if (list.isNotEmpty()) list.map { it.currentProgress }.average().toFloat() else 75f }

    val marketProgress = goals.filter { it.category == GoalCategory.MARKET }
        .let { list -> if (list.isNotEmpty()) list.map { it.currentProgress }.average().toFloat() else 60f }

    val careerProgress = goals.filter { it.category == GoalCategory.CAREER }
        .let { list -> if (list.isNotEmpty()) list.map { it.currentProgress }.average().toFloat() else 85f }

    val numberFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 2
        }
    }

    var isPortalsExpanded by remember { mutableStateOf(true) }
    var isChartExpanded by remember { mutableStateOf(false) }
    var isProgressExpanded by remember { mutableStateOf(false) }
    var isGoalsExpanded by remember { mutableStateOf(false) }
    var isEventsExpanded by remember { mutableStateOf(false) }

    // Post media prompt & upload states
    var showPostChoicePrompt by remember { mutableStateOf(false) }
    var capturedPhotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var showPostPublishModal by remember { mutableStateOf(false) }
    var postCaption by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedPhotoBitmap = bitmap
            selectedFileUri = null
            showPostPublishModal = true
        }
    }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFileUri = uri
            capturedPhotoBitmap = null
            showPostPublishModal = true
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_bar_title")
                    ) {
                        XportLogo(markSize = 42.dp, showSubtitle = false)
                        Spacer(modifier = Modifier.width(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (analyticsState.activeStreamTicking) PrimaryBrand.copy(alpha = 0.12f) else AccentRed.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (analyticsState.activeStreamTicking) PrimaryBrand else AccentRed)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (analyticsState.activeStreamTicking) "LIVE" else "PAUSED",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = if (analyticsState.activeStreamTicking) PrimaryBrand else AccentRed,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.toggleSidebarExpanded() },
                        modifier = Modifier.testTag("menu_sidebar_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Toggle Navigation Sidebar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Sign In / Log In Button (or Profile badge if logged in)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (authUser.isLoggedIn) PrimaryBrand.copy(alpha = 0.15f) else PrimaryBrand,
                        border = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setShowAuthDialog(true) }
                            .testTag("auth_action_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (authUser.isLoggedIn) {
                                Text(text = authUser.avatarEmoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = authUser.name.split(" ").firstOrNull() ?: "User",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Login,
                                    contentDescription = "Sign In / Log In",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Sign In / Log In",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { viewModel.toggleStreamTicking() },
                        modifier = Modifier.testTag("stream_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (analyticsState.activeStreamTicking) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (analyticsState.activeStreamTicking) "Pause Stream" else "Resume Stream",
                            tint = if (analyticsState.activeStreamTicking) PrimaryBrand else AccentRed
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            // Post Button: triggers prompt asking whether to use camera or files
            FloatingActionButton(
                onClick = { showPostChoicePrompt = true },
                containerColor = PrimaryBrand,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("post_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Post",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Post",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        // Main Screen Structure: Same-Screen Sidebar + Main Dashboard Content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Same-Screen Navigation Sidebar for 1-tap quick report access
            SameScreenSidebar(
                isExpanded = isSidebarExpanded,
                onToggleExpanded = { viewModel.toggleSidebarExpanded() },
                onOpenPortalModal = { portal -> viewModel.openPortalModal(portal) },
                activePortal = activeModalPortal
            )

            // Main Dashboard Content Area with Fixed & Floating Portal Cards over Scrolling Contacts
            val configuration = androidx.compose.ui.platform.LocalConfiguration.current
            val portalCardHeight = (configuration.screenHeightDp * 0.25f).dp.coerceAtLeast(180.dp)
            val floatingPortalsTopOffset = if (isPortalsExpanded) portalCardHeight + 58.dp else 68.dp

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Main Dashboard Analytics & Progress Scroll Area (scrolls under the floating portal cards)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("dashboard_scroll_column"),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = floatingPortalsTopOffset,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Section 1: Contacts & Chat Hub (Comes directly after the portal cards, scrolls under them)
                    item {
                        HomeContactsHub(
                            contacts = contacts,
                            hasPhonePermission = hasPhonePermission,
                            onRequestPhonePermission = { viewModel.requestPhoneContactsSync() },
                            onOpenChat = { contact -> viewModel.openChat(contact) },
                            onQuickGreet = { contact ->
                                viewModel.sendMessage(contact.id, "👋 Hi ${contact.name}! Hope you're having an awesome day! 🎉")
                                viewModel.openChat(contact)
                            }
                        )
                    }

                    // Section 2: Final App Walkthrough & Video Showcase Player (Collapsible & Minimized)
                    item {
                        AppShowcaseVideoSection(
                            onOpenPortalModal = { portal -> viewModel.openPortalModal(portal) }
                        )
                    }

                // Section 4: Minimized Real-Time KPI Telemetry Cards (Compact 1-row summary)
                item {
                    MinimizedKpiBar(
                        analyticsState = analyticsState,
                        completedGoals = completedGoals,
                        totalGoals = totalGoals
                    )
                }

                // Section 5: Interactive Real-Time Sparkline Chart (Collapsible)
                item {
                    if (!isChartExpanded) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { isChartExpanded = true }
                                .testTag("telemetry_graph_minimized_card"),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = null,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBrand.copy(alpha = 0.16f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TrendingUp,
                                            contentDescription = null,
                                            tint = PrimaryBrand,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "REAL-TIME TELEMETRY GRAPH",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "Live Stream: $128,450.00 • Tap to view chart",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Chart",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "REAL-TIME TELEMETRY CHART",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                IconButton(
                                    onClick = { isChartExpanded = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Minimize Chart",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            RealtimeSparklineChart(
                                points = analyticsState.livePoints,
                                selectedTimeRange = selectedTimeRange,
                                onTimeRangeSelected = { viewModel.selectTimeRange(it) },
                                isLiveTicking = analyticsState.activeStreamTicking,
                                lineColor = PrimaryBrand
                            )
                        }
                    }
                }

                // Section 6: User Progress Tracking & Milestone Rings (Collapsible)
                item {
                    if (!isProgressExpanded) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { isProgressExpanded = true }
                                .testTag("progress_overview_minimized_card"),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = null,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldSuccess.copy(alpha = 0.16f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoGraph,
                                            contentDescription = null,
                                            tint = EmeraldSuccess,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "USER PROGRESS & XP LEVEL",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "Level 8 Strategist • ${(overallCompletionRate * 100).toInt()}% Completion",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Stats",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "USER PROGRESS TRACKING & MILESTONE RINGS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                IconButton(
                                    onClick = { isProgressExpanded = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Minimize Progress",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            UserProgressOverview(
                                overallCompletionRate = overallCompletionRate,
                                investmentProgress = investmentProgress,
                                marketProgress = marketProgress,
                                careerProgress = careerProgress,
                                streakDays = 14,
                                levelNumber = 8,
                                levelTitle = "Growth Strategist",
                                currentXp = 4250,
                                targetXp = 5000
                            )
                        }
                    }
                }

                // Section 7: Interactive Goal Milestones Management (Collapsible)
                item {
                    if (!isGoalsExpanded) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { isGoalsExpanded = true }
                                .testTag("goals_minimized_card"),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = null,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBrand.copy(alpha = 0.16f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = PrimaryBrand,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "GOAL MILESTONES (${goals.size})",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "$completedGoals of $totalGoals completed • Tap to view all",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { viewModel.setShowAddGoalDialog(true) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(26.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = PrimaryBrand
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Add", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { isGoalsExpanded = true },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Expand Goals",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "USER PROGRESS & MILESTONES (${goals.size})",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { viewModel.setShowAddGoalDialog(true) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(28.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = PrimaryBrand
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { isGoalsExpanded = false },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Minimize Goals",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (isGoalsExpanded) {
                    items(goals, key = { it.id }) { goal ->
                        GoalItemCard(
                            goal = goal,
                            onToggleCompleted = { viewModel.toggleGoalCompleted(goal) },
                            onIncrementProgress = { viewModel.updateGoalProgress(goal, 10f) },
                            onDecrementProgress = { viewModel.updateGoalProgress(goal, -10f) },
                            onDelete = { viewModel.deleteGoal(goal) }
                        )
                    }
                }

                // Section 8: Real-Time Live Activity & Alert Stream (Collapsible)
                item {
                    if (!isEventsExpanded) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { isEventsExpanded = true }
                                .testTag("activity_stream_minimized_card"),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = null,
                            tonalElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryYellow.copy(alpha = 0.16f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = null,
                                            tint = PrimaryYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "LIVE EVENT & ALERT STREAM",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "${analyticsState.activityStream.size} Real-Time Events • Tap to view feed",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Feed",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "LIVE EVENT & ALERT STREAM",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Real-Time Feed",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PrimaryYellow,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(
                                        onClick = { isEventsExpanded = false },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Minimize Events",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = null,
                                tonalElevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    if (analyticsState.activityStream.isEmpty()) {
                                        Text(
                                            text = "Connecting to real-time analytics events...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = 12.dp)
                                        )
                                    } else {
                                        analyticsState.activityStream.take(6).forEachIndexed { index, event ->
                                            ActivityEventRow(event = event)
                                            if (index < analyticsState.activityStream.take(6).lastIndex) {
                                                HorizontalDivider(
                                                    color = MaterialTheme.colorScheme.outlineVariant,
                                                    modifier = Modifier.padding(vertical = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom spacer for FAB clearance
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

            // Fixed & Floating Portal Cards over scrolling contacts & dashboard feed
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .zIndex(10f),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
                shadowElevation = 8.dp,
                border = null
            ) {
                Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    if (!isPortalsExpanded) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .testTag("quick_portals_compact_bar"),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = null,
                            tonalElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Market Pill
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PortalType.MARKET.accentColor.copy(alpha = 0.15f),
                                        border = null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.openPortalModal(PortalType.MARKET) }
                                            .testTag("compact_portal_market")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = null,
                                                tint = PortalType.MARKET.accentColor,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Market",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PortalType.MARKET.accentColor
                                            )
                                        }
                                    }

                                    // Investment Pill
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PortalType.INVESTMENT.accentColor.copy(alpha = 0.15f),
                                        border = null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.openPortalModal(PortalType.INVESTMENT) }
                                            .testTag("compact_portal_investment")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.TrendingUp,
                                                contentDescription = null,
                                                tint = PortalType.INVESTMENT.accentColor,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Invest",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PortalType.INVESTMENT.accentColor
                                            )
                                        }
                                    }

                                    // Service Pill
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PortalType.SERVICE.accentColor.copy(alpha = 0.15f),
                                        border = null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.openPortalModal(PortalType.SERVICE) }
                                            .testTag("compact_portal_service")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Work,
                                                contentDescription = null,
                                                tint = PortalType.SERVICE.accentColor,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Service",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PortalType.SERVICE.accentColor
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                IconButton(
                                    onClick = { isPortalsExpanded = true },
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand Portals",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "QUICK REPORT PORTALS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSidebarExpanded) PrimaryBrand.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                        border = null,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.toggleSidebarExpanded() }
                                            .testTag("toggle_portal_hub_header_button")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                contentDescription = null,
                                                tint = if (isSidebarExpanded) PrimaryBrand else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isSidebarExpanded) "Portal Hub Active" else "Open Portal Hub",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSidebarExpanded) PrimaryBrand else MaterialTheme.colorScheme.onSurface,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { isPortalsExpanded = false },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Minimize Portals",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            // All portal cards horizontally next to each other fitting completely in the screen (1/4 screen height)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("portal_cards_horizontal_row"),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PortalShortcutCard(
                                    portal = PortalType.SERVICE,
                                    onClick = { viewModel.openPortalModal(PortalType.SERVICE) },
                                    modifier = Modifier.weight(1f)
                                )
                                PortalShortcutCard(
                                    portal = PortalType.MARKET,
                                    onClick = { viewModel.openPortalModal(PortalType.MARKET) },
                                    modifier = Modifier.weight(1f)
                                )
                                PortalShortcutCard(
                                    portal = PortalType.INVESTMENT,
                                    onClick = { viewModel.openPortalModal(PortalType.INVESTMENT) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        } // Closes Box containing LazyColumn and floating Surface
    } // Closes Row containing SameScreenSidebar and Box
} // Closes Scaffold innerPadding lambda

    // Modal View for Web Reports (Investment Portal, Market Portal, Service Portal)
    activeModalPortal?.let { portal ->
        WebPortalModal(
            portalType = portal,
            cachedProducts = cachedMarketProducts,
            onProductPurchased = { productId -> viewModel.recordProductPurchase(productId) },
            isRoomCached = isRoomCacheReady,
            userPosts = userPosts,
            walletBalance = authUser.walletBalance,
            deliveryManager = viewModel.deliveryManager,
            onTopUpWallet = { amount -> viewModel.depositWallet(amount) },
            onDismiss = { viewModel.closePortalModal() }
        )
    }

    // Add Goal Dialog Modal
    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { viewModel.setShowAddGoalDialog(false) },
            onConfirm = { newGoal -> viewModel.addGoal(newGoal) }
        )
    }

    // Sign In / Log In & Profile Auth Dialog
    if (showAuthDialog) {
        AuthDialog(
            currentUser = authUser,
            onDismiss = { viewModel.setShowAuthDialog(false) },
            onLogin = { email, name -> viewModel.login(email, name) },
            onLogout = { viewModel.logout() }
        )
    }

    // Interactive Real-Time Contact Chat Modal
    activeChatContact?.let { contact ->
        ChatConversationDialog(
            contact = contact,
            messages = messagesMap[contact.id] ?: emptyList(),
            onSendMessage = { text -> viewModel.sendMessage(contact.id, text) },
            onDismiss = { viewModel.closeChat() }
        )
    }

    // Post Media Choice Prompt (Camera or Files)
    if (showPostChoicePrompt) {
        PostMediaChoiceDialog(
            onDismiss = { showPostChoicePrompt = false },
            onSelectCamera = {
                showPostChoicePrompt = false
                cameraLauncher.launch(null)
            },
            onSelectFiles = {
                showPostChoicePrompt = false
                fileLauncher.launch("*/*")
            }
        )
    }

    // Post Preview & Publish Modal
    if (showPostPublishModal) {
        PostPublishDialog(
            capturedPhotoBitmap = capturedPhotoBitmap,
            selectedFileUri = selectedFileUri,
            caption = postCaption,
            onCaptionChange = { postCaption = it },
            onDismiss = {
                showPostPublishModal = false
                capturedPhotoBitmap = null
                selectedFileUri = null
                postCaption = ""
            },
            onPublish = {
                showPostPublishModal = false
                capturedPhotoBitmap = null
                selectedFileUri = null
                postCaption = ""
                Toast.makeText(context, "Post shared successfully to your feed!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun PortalShortcutCard(
    portal: PortalType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val cardHeight = (configuration.screenHeightDp * 0.25f).dp.coerceAtLeast(180.dp)
    val carouselItems = remember(portal) { getPortalCarouselMedia(portal) }
    val carouselState = rememberPagerState(pageCount = { carouselItems.size })

    val shortName = when (portal) {
        PortalType.MARKET -> "Market"
        PortalType.INVESTMENT -> "Invest"
        PortalType.SERVICE, PortalType.JOB -> "Services"
        PortalType.CUSTOM -> "Web"
    }

    Card(
        modifier = modifier
            .height(cardHeight)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("portal_shortcut_${portal.name.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = null,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // High-Res Media Carousel taking the full 1/4 screen card height
            HorizontalPager(
                state = carouselState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val media = carouselItems[page]
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = media.imageUrl,
                        contentDescription = media.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // High-contrast gradient overlay for crystal clear text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.55f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )

                    // Center Play Button overlay for videos
                    if (media.isVideo) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Video Reel",
                                tint = portal.accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Media Title & Subtitle overlay at bottom of carousel item
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = portal.accentColor.copy(alpha = 0.25f),
                            border = null
                        ) {
                            Text(
                                text = shortName.uppercase(),
                                color = portal.accentColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = media.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            lineHeight = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Top Header Overlay inside Media Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp, vertical = 7.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(portal.accentColor.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = portal.getIcon(),
                        contentDescription = portal.title,
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }

                // Carousel Page Indicator Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    border = null
                ) {
                    val currentMedia = carouselItems.getOrNull(carouselState.currentPage)
                    val typeLabel = if (currentMedia?.isVideo == true) "▶" else "📷"
                    Text(
                        text = "$typeLabel ${carouselState.currentPage + 1}/${carouselItems.size}",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            // Carousel Dot Indicators at Bottom End
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(carouselItems.size) { idx ->
                    val isSelected = carouselState.currentPage == idx
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 5.dp else 3.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) portal.accentColor else Color.White.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
fun PostMediaChoiceDialog(
    onDismiss: () -> Unit,
    onSelectCamera: () -> Unit,
    onSelectFiles: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = null,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("post_choice_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(PrimaryBrand.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = PrimaryBrand,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Create New Post",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Where would you like to get your media from?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Camera Option Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectCamera() }
                        .testTag("post_choice_camera_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(PrimaryBrand),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Camera",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Capture photo or live video reel",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PrimaryBrand,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Files Option Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectFiles() }
                        .testTag("post_choice_files_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DarkYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "Files",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Files / Storage",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Browse images, video clips, and files",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = DarkYellow,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun PostPublishDialog(
    capturedPhotoBitmap: Bitmap?,
    selectedFileUri: Uri?,
    caption: String,
    onCaptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onPublish: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = null,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("post_publish_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ready to Publish",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Media Preview Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (capturedPhotoBitmap != null) {
                        Image(
                            bitmap = capturedPhotoBitmap.asImageBitmap(),
                            contentDescription = "Captured Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (selectedFileUri != null) {
                        AsyncImage(
                            model = selectedFileUri,
                            contentDescription = "Selected File",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = if (capturedPhotoBitmap != null) "📸 Camera Capture" else "📁 Selected File",
                            color = PrimaryBrand,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                androidx.compose.material3.OutlinedTextField(
                    value = caption,
                    onValueChange = onCaptionChange,
                    placeholder = { Text("Write a caption or description...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_caption_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = null,
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Discard")
                    }

                    Button(
                        onClick = onPublish,
                        modifier = Modifier.weight(1f).testTag("post_confirm_publish_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrand, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Post Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItemCard(
    goal: GoalEntity,
    onToggleCompleted: () -> Unit,
    onIncrementProgress: () -> Unit,
    onDecrementProgress: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryColor = when (goal.category) {
        GoalCategory.INVESTMENT -> AccentRed
        GoalCategory.MARKET -> PrimaryYellow
        GoalCategory.CAREER -> DarkYellow
        GoalCategory.GENERAL -> PrimaryYellow
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = null,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onToggleCompleted,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (goal.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (goal.isCompleted) "Completed" else "Incomplete",
                            tint = if (goal.isCompleted) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = categoryColor.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = goal.category.name,
                                    color = categoryColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = goal.dueDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (goal.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (goal.notes.isNotBlank()) {
                Text(
                    text = goal.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 38.dp, top = 2.dp, bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Progress Slider / Quick Modifier Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 38.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                        OdometerText(
                            text = "${goal.currentProgress.toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            digitKind = DigitKind.PERCENTAGE,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (goal.currentProgress / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = categoryColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Quick Increment/Decrement Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = onDecrementProgress)
                    ) {
                        Text(
                            text = "-10%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = categoryColor.copy(alpha = 0.2f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = onIncrementProgress)
                    ) {
                        Text(
                            text = "+10%",
                            style = MaterialTheme.typography.labelSmall,
                            color = categoryColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityEventRow(event: LiveActivityEvent) {
    val accentColor = when (event.type) {
        com.example.data.EventType.INVESTMENT -> AccentRed
        com.example.data.EventType.MARKET_TICK -> PrimaryYellow
        com.example.data.EventType.CAREER_ACHIEVEMENT -> DarkYellow
        com.example.data.EventType.SYSTEM_ALERT -> AccentRed
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = event.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = event.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
            if (event.delta != null) {
                OdometerText(
                    text = event.delta,
                    style = MaterialTheme.typography.labelSmall,
                    digitKind = if (event.delta.startsWith("+")) DigitKind.DELTA_POS else DigitKind.DELTA_NEG,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}
