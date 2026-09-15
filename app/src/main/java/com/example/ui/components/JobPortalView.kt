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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.data.UserAccountPost
import com.example.portals.PortalType
import com.example.util.CurrencyUtils
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkYellow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.PrimaryBrand
import com.example.ui.theme.PrimaryBrandDark
import com.example.ui.theme.PrimaryYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class JobListing(
    val id: String,
    val title: String,
    val company: String,
    val companyHandle: String,
    val category: String,
    val minSalary: Int,
    val maxSalary: Int,
    val location: String,
    val type: String,
    val experienceLevel: String,
    val timeAgo: String,
    val initialApplicantCount: Int,
    val skills: List<String>,
    val description: String,
    val responsibilities: List<String>,
    val perks: List<String>,
    val adminVerified: Boolean = true,
    val defaultMediaType: CardMediaType = CardMediaType.VIDEO,
    val companyEmoji: String = "🏢",
    val brandColor: Color = PrimaryBrand,
    val photoUrls: List<String> = emptyList(),
    val videoPreviewUrl: String? = null
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobPortalView(
    onClose: () -> Unit,
    userPosts: List<UserAccountPost> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Incorporate posts published from Service Accounts
    val servicePostsAsListings = remember(userPosts) {
        userPosts.filter { it.targetPortal == PortalType.SERVICE }.map { post ->
            JobListing(
                id = post.id,
                title = post.title,
                company = post.authorName,
                companyHandle = "@${post.authorName.lowercase().replace(" ", "")}",
                category = post.category,
                minSalary = (post.priceOrRate * 1000).toInt(),
                maxSalary = (post.priceOrRate * 1500).toInt(),
                location = "Worldwide / Remote",
                type = "Direct Service",
                experienceLevel = "Verified Specialist",
                timeAgo = "Just now",
                initialApplicantCount = 14,
                skills = listOf("Verified Pro", "Instant Hire", post.category),
                responsibilities = listOf("Book immediately via BUY button", "Milestone escrow protection enabled"),
                perks = listOf("Money Back Guarantee", "Direct Communication", "Fast Turnaround"),
                description = post.description,
                videoPreviewUrl = "https://assets.mixkit.co/videos/preview/mixkit-software-developer-working-on-code-42898-large.mp4",
                photoUrls = listOf(if (post.photoUrl.isNotBlank()) post.photoUrl else "https://images.unsplash.com/photo-1551434678-e076c223a692?auto=format&fit=crop&w=800&q=80"),
                brandColor = PrimaryBrand,
                adminVerified = true,
                defaultMediaType = CardMediaType.IMAGE
            )
        }
    }

    val combinedJobListings = remember(servicePostsAsListings) {
        servicePostsAsListings + sampleJobListings
    }

    val allCategories = remember {
        listOf(
            "All Services",
            "Tech & AI",
            "Engineering",
            "Finance",
            "Design & UX",
            "Marketing",
            "Product"
        )
    }

    var selectedCategory by remember { mutableStateOf("All Services") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Dynamic applicant counter map
    val applicantCounts = remember(combinedJobListings) {
        mutableStateMapOf<String, Int>().apply {
            combinedJobListings.forEach { put(it.id, it.initialApplicantCount) }
        }
    }
    val appliedJobIds = remember { mutableStateMapOf<String, Boolean>() }
    val savedJobIds = remember { mutableStateMapOf<String, Boolean>() }
    // Account user media preference map (Video vs Image)
    val mediaTypePreferences = remember(combinedJobListings) {
        mutableStateMapOf<String, CardMediaType>().apply {
            combinedJobListings.forEach { put(it.id, it.defaultMediaType) }
        }
    }

    val filteredJobs = remember(selectedCategory, searchQuery, combinedJobListings) {
        combinedJobListings.filter { job ->
            val matchesCategory = selectedCategory == "All Services" || job.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    job.title.contains(searchQuery, ignoreCase = true) ||
                    job.company.contains(searchQuery, ignoreCase = true) ||
                    job.location.contains(searchQuery, ignoreCase = true) ||
                    job.skills.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }
    }

    val infinitePageCount = if (filteredJobs.isNotEmpty()) 100_000 else 1
    val initialPage = if (filteredJobs.isNotEmpty()) 50_000 - (50_000 % filteredJobs.size) else 0
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { infinitePageCount })
    val coroutineScope = rememberCoroutineScope()

    // Root Container: Card takes 100% of the entire screen
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07070A))
            .testTag("job_portal_view")
    ) {
        // Vertical Pager: FULL SCREEN CARD TAKES 100% OF VIEWPORT IN CONTINUOUS INFINITE LOOP
        if (filteredJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No services matched '$searchQuery'",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try switching service categories or clearing search keywords.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            searchQuery = ""
                            selectedCategory = "All Services"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrand, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Filters", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("job_vertical_pager")
            ) { page ->
                val actualIndex = page % filteredJobs.size
                val job = filteredJobs[actualIndex]
                val currentApplicants = applicantCounts[job.id] ?: job.initialApplicantCount
                val isApplied = appliedJobIds[job.id] == true
                val isSaved = savedJobIds[job.id] == true
                val mediaType = mediaTypePreferences[job.id] ?: job.defaultMediaType

                FullScreenJobMediaCard(
                    job = job,
                    mediaType = mediaType,
                    currentApplicants = currentApplicants,
                    isApplied = isApplied,
                    isSaved = isSaved,
                    pageIndex = actualIndex,
                    totalPages = filteredJobs.size,
                    onToggleMediaType = {
                        val nextType = if (mediaType == CardMediaType.VIDEO) CardMediaType.IMAGE else CardMediaType.VIDEO
                        mediaTypePreferences[job.id] = nextType
                    },
                    onApply = {
                        if (!isApplied) {
                            appliedJobIds[job.id] = true
                            applicantCounts[job.id] = currentApplicants + 1
                        }
                    },
                    onToggleSave = {
                        savedJobIds[job.id] = !(isSaved)
                    },
                    onScrollNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
            }
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
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Close Button (Translucent glass)
                    Surface(
                        shape = CircleShape,
                        color = Color(0x66181824),
                        border = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onClose() }
                            .testTag("job_portal_close_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Job Portal",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Center: Translucent Brand Pill
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
                                text = "OPPORTUNITIES",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 1.sp,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(12.dp)
                                    .background(Color(0x44FFFFFF))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${filteredJobs.size} Active",
                                color = PrimaryBrand,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Right: Saved Roles Counter (Translucent)
                    val savedCount = savedJobIds.values.count { it }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (savedCount > 0) PrimaryBrand.copy(alpha = 0.25f) else Color(0x66181824),
                        border = null,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Saved Jobs",
                                tint = if (savedCount > 0) PrimaryBrand else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            OdometerText(
                                text = "$savedCount",
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
                            placeholder = { Text("Search title, company, skills...", fontSize = 11.sp, color = Color.LightGray) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("job_search_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBrand,
                                unfocusedBorderColor = Color(0x55FFFFFF),
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
                                    .testTag("job_category_${category.lowercase().replace(" ", "_")}")
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
                                .testTag("job_floating_search_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Search Jobs",
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
 * FULL SCREEN IMMERSIVE JOB CARD
 * Takes 100% of the screen. Supports dynamic Video Pitch and Workspace Photo Gallery modes.
 * Words are neatly organized in a bottom scrim and right HUD rail so the image/video remains crystal clear!
 */
@Composable
private fun FullScreenJobMediaCard(
    job: JobListing,
    mediaType: CardMediaType,
    currentApplicants: Int,
    isApplied: Boolean,
    isSaved: Boolean,
    pageIndex: Int,
    totalPages: Int,
    onToggleMediaType: () -> Unit,
    onApply: () -> Unit,
    onToggleSave: () -> Unit,
    onScrollNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var activeImageIndex by remember { mutableIntStateOf(0) }
    var showDetailsModal by remember { mutableStateOf(false) }

    // Dynamic Video playback simulation progress (0f to 1f)
    var videoProgress by remember { mutableFloatStateOf(0.24f) }
    var videoCurrentSeconds by remember { mutableIntStateOf(8) }

    LaunchedEffect(isPlaying, mediaType) {
        if (mediaType == CardMediaType.VIDEO && isPlaying) {
            while (true) {
                delay(1000)
                videoCurrentSeconds = (videoCurrentSeconds + 1) % 35
                videoProgress = videoCurrentSeconds / 35f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .testTag("job_card_${job.id}")
    ) {
        // ==========================================
        // 1. FULL-SCREEN BACKGROUND MEDIA CANVAS (Video or Image)
        // Takes the whole screen from top to bottom
        // ==========================================
        if (mediaType == CardMediaType.VIDEO) {
            FullScreenJobVideoCanvas(
                job = job,
                isPlaying = isPlaying,
                isMuted = isMuted,
                videoProgress = videoProgress,
                onTogglePlayPause = { isPlaying = !isPlaying }
            )
        } else {
            FullScreenJobImageCanvas(
                job = job,
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
                label = if (mediaType == CardMediaType.VIDEO) "PITCH" else "OFFICE",
                isActive = true,
                activeColor = PrimaryBrand,
                onClick = onToggleMediaType,
                testTag = "job_media_toggle_${job.id}"
            )

            // Save / Bookmark Role Button
            TranslucentRailButton(
                icon = if (isSaved) Icons.Default.Star else Icons.Default.StarBorder,
                label = if (isSaved) "SAVED" else "SAVE",
                isActive = isSaved,
                activeColor = PrimaryBrand,
                onClick = onToggleSave,
                testTag = "save_job_${job.id}"
            )

            // Full Details & Perks Modal Button
            TranslucentRailButton(
                icon = Icons.Default.Info,
                label = "INFO",
                isActive = false,
                onClick = { showDetailsModal = true },
                testTag = "info_job_${job.id}"
            )

            // Scroll to next service button in infinite loop
            if (totalPages > 1) {
                TranslucentRailButton(
                    icon = Icons.Default.KeyboardArrowDown,
                    label = "NEXT",
                    isActive = false,
                    onClick = onScrollNext,
                    testTag = "next_job_${job.id}"
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
                // Company Account Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Company Avatar
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(job.brandColor.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = job.brandColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = job.company,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                if (job.adminVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified Employer",
                                        tint = PrimaryBrand,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = job.companyHandle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA2A2B0),
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${job.timeAgo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldSuccess,
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
                                    text = "TEAM PITCH • 00:${videoCurrentSeconds.toString().padStart(2, '0')}/00:35",
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
                                    text = "WORKSPACE ${activeImageIndex + 1}/3",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Job Title
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Salary & Applicant Metrics Row: Dual Currency (USD & UGX) + Applicant Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess
                    )
                    OdometerText(
                        text = "${job.minSalary / 1000}k - $${job.maxSalary / 1000}k",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        digitKind = DigitKind.MONEY,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EmeraldSuccess.copy(alpha = 0.18f),
                        border = null
                    ) {
                        val minUgxM = (job.minSalary * CurrencyUtils.UGX_PER_USD / 1_000_000).toInt()
                        val maxUgxM = (job.maxSalary * CurrencyUtils.UGX_PER_USD / 1_000_000).toInt()
                        Text(
                            text = "UGX ${minUgxM}M-${maxUgxM}M",
                            color = EmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = " / yr",
                        color = Color(0xFFA0A0B0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Applicant Counter Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x66000000),
                        border = null
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OdometerText(
                                text = "$currentApplicants",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                digitKind = DigitKind.COUNT,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "applied",
                                color = Color(0xFFC0C0D0),
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Location, Type & Skills Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x551B1B26),
                        border = null
                    ) {
                        Text(
                            text = "${job.location} • ${job.type}",
                            color = Color(0xFFDCDCE8),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    job.skills.take(2).forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0x551B1B26),
                            border = null
                        ) {
                            Text(
                                text = skill,
                                color = PrimaryBrand,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Bar: Scope Overview on Left + BUY Button on Right Bottom Corner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick Details Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x66181826),
                        border = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showDetailsModal = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "View Info",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Service Scope",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Button on each card on the right bottom corner called BUY
                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("service_buy_button_${job.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isApplied) EmeraldSuccess else PrimaryBrand,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isApplied) Icons.Default.CheckCircle else Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isApplied) "PURCHASED ✓" else "BUY",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 4. DETAILED ROLE & PERKS MODAL BOTTOM SHEET
        // ==========================================
        if (showDetailsModal) {
            JobDetailsSheet(
                job = job,
                onDismiss = { showDetailsModal = false }
            )
        }
    }
}

/**
 * FULL-SCREEN JOB VIDEO CANVAS SIMULATION
 * Features animated company demo reel, audio wave visualizer, timeline progress, and video HUD.
 */
@Composable
private fun FullScreenJobVideoCanvas(
    job: JobListing,
    isPlaying: Boolean,
    isMuted: Boolean,
    videoProgress: Float,
    onTogglePlayPause: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "job_video_motion")
    val ambientSweep by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "job_ambient_sweep"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onTogglePlayPause() },
        contentAlignment = Alignment.Center
    ) {
        // Real-Life Photography Backdrop for Video if available, else dynamic canvas
        if (job.photoUrls.isNotEmpty()) {
            AsyncImage(
                model = job.photoUrls.first(),
                contentDescription = "${job.company} Workspace Video Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color(0x33000000),
                                Color(0xDD0A0A10)
                            )
                        )
                    )
            )
        } else {
            // Dynamic Studio Lighting Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Center glow following simulated office/tech studio light
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            job.brandColor.copy(alpha = 0.2f),
                            Color(0x181820).copy(alpha = 0.4f),
                            Color(0xFF0A0A10)
                        ),
                        center = Offset(w * 0.5f, h * 0.45f),
                        radius = w * 0.75f
                    )
                )

                // Animated subtle tech scan lines
                val scanY = (h * (videoProgress % 1f))
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, job.brandColor.copy(alpha = 0.35f), Color.Transparent)
                    ),
                    start = Offset(0f, scanY),
                    end = Offset(w, scanY),
                    strokeWidth = 2f
                )
            }
        }

        // Main Center Video Presentation - Stickers made invisible so video/backdrop is completely unobstructed
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Resume Video",
                    tint = PrimaryBrand,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Video Progress Timeline Indicator
        LinearProgressIndicator(
            progress = { videoProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.TopCenter)
                .padding(top = 90.dp),
            color = PrimaryBrand,
            trackColor = Color(0x33FFFFFF)
        )
    }
}

/**
 * FULL-SCREEN JOB PHOTO GALLERY CANVAS SIMULATION
 * Features high-res architecture, team lab photos, and photo cycle indicators.
 */
@Composable
private fun FullScreenJobImageCanvas(
    job: JobListing,
    activeImageIndex: Int,
    onCycleImage: () -> Unit
) {
    val photoAngles = listOf("Silicon Valley HQ Studio", "Hardware & Cloud Lab", "Engineering Team Floor")
    val currentPhotoUrl = if (job.photoUrls.isNotEmpty()) {
        job.photoUrls[activeImageIndex % job.photoUrls.size]
    } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCycleImage() },
        contentAlignment = Alignment.Center
    ) {
        if (currentPhotoUrl != null) {
            // Real-life High-Resolution Photography Backdrop
            AsyncImage(
                model = currentPhotoUrl,
                contentDescription = "${job.company} Workspace Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Cinematic Dark Gradient Overlay for Readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color(0x33000000),
                                Color(0xD90A0A10)
                            )
                        )
                    )
            )
        } else {
            // Fallback Workspace Photography Backdrop Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF20202E),
                            Color(0xFF13131D),
                            Color(0xFF07070B)
                        ),
                        center = Offset(w * 0.5f, h * 0.42f),
                        radius = w * 0.85f
                    )
                )

                val chSize = 14f
                val cx = w * 0.5f
                val cy = h * 0.42f
                drawLine(Color(0x33FFFFFF), Offset(cx - chSize, cy), Offset(cx + chSize, cy), 1.5f)
                drawLine(Color(0x33FFFFFF), Offset(cx, cy - chSize), Offset(cx, cy + chSize), 1.5f)
            }
        }

        // Center High-Res Photo Display - Stickers made invisible so photography is unobstructed
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        ) {

            // Photo Angle Pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xCC000000),
                border = null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Collections,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val angleText = photoAngles.getOrElse(activeImageIndex % photoAngles.size) { "Workspace Tour" }
                    Text(
                        text = "📸 $angleText • Tap to Cycle",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Detailed Job Scope & Perks Modal Bottom Sheet
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun JobDetailsSheet(
    job: JobListing,
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
                        text = "Role Overview & Scope",
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
                    text = job.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD4D4E0),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "CORE RESPONSIBILITIES",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryBrand,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                job.responsibilities.forEach { resp ->
                    Row(modifier = Modifier.padding(vertical = 3.dp)) {
                        Text(text = "• ", color = PrimaryBrand, fontWeight = FontWeight.Bold)
                        Text(text = resp, color = Color(0xFFDCDCE8), fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "COMPENSATION & PERKS",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldSuccess,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    job.perks.forEach { perk ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E1E2C),
                            border = null
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = perk, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleJobListings = listOf(
    JobListing(
        id = "job_1",
        title = "Staff AI & Machine Learning Systems Engineer",
        company = "Apex Intelligence Labs",
        companyHandle = "@apex_ai",
        category = "Tech & AI",
        minSalary = 185000,
        maxSalary = 240000,
        location = "San Francisco, CA / Remote",
        type = "Full Time",
        experienceLevel = "Senior / Staff",
        timeAgo = "2h ago",
        initialApplicantCount = 28,
        skills = listOf("PyTorch", "LLMs", "Distributed Training", "CUDA", "Kotlin", "FastAPI"),
        description = "Design and scale low-latency real-time inference clusters, on-device model quantization, and distributed training pipelines for next-generation intelligence feeds.",
        responsibilities = listOf(
            "Architect high-throughput real-time AI model serving pipelines with sub-20ms latency",
            "Implement model compression, weight distillation, and streaming inference loops",
            "Collaborate with mobile systems engineers to bring on-device intelligence to mobile apps"
        ),
        perks = listOf("0.5% - 1.2% Equity", "Unlimited PTO", "Full Healthcare", "$5,000 Home Office Stipend"),
        companyEmoji = "🧠",
        defaultMediaType = CardMediaType.VIDEO,
        brandColor = PrimaryBrand,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1497366216548-37526070297c?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    ),
    JobListing(
        id = "job_2",
        title = "Principal Mobile Architect (Kotlin & Jetpack Compose)",
        company = "Velox Systems",
        companyHandle = "@velox_tech",
        category = "Engineering",
        minSalary = 160000,
        maxSalary = 210000,
        location = "New York, NY / Hybrid",
        type = "Full Time",
        experienceLevel = "Lead / Principal",
        timeAgo = "4h ago",
        initialApplicantCount = 42,
        skills = listOf("Jetpack Compose", "Coroutines & Flow", "Room DB", "M3 Design System", "CI/CD"),
        description = "Lead the engineering and UI architecture for our high-frequency streaming analytics app, crafting ultra-smooth animations, local persistence engines, and modular architectures.",
        responsibilities = listOf(
            "Lead end-to-end design of scalable Compose design systems and state persistence",
            "Optimize frame rendering rates and background synchronization pipelines",
            "Mentor senior engineers and set high standards for UI testability and craft"
        ),
        perks = listOf("Competitive Equity", "Annual Learning Budget", "Comprehensive 401(k) Match"),
        companyEmoji = "📱",
        defaultMediaType = CardMediaType.VIDEO,
        brandColor = AccentRed,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    ),
    JobListing(
        id = "job_3",
        title = "Senior Quantitative Portfolio & Yield Analyst",
        company = "Vanguard Horizon Capital",
        companyHandle = "@horizon_quant",
        category = "Finance",
        minSalary = 175000,
        maxSalary = 230000,
        location = "Chicago, IL / Remote",
        type = "Full Time",
        experienceLevel = "Senior",
        timeAgo = "5h ago",
        initialApplicantCount = 19,
        skills = listOf("Quantitative Modeling", "Python", "Risk Analytics", "Derivatives", "SQL"),
        description = "Develop algorithmic models to predict yield variances, portfolio risk indexes, and dynamic real-time liquidity rebalancing for multi-asset funds.",
        responsibilities = listOf(
            "Build automated real-time yield valuation algorithms and scenario stress models",
            "Interface with trading desks to optimize deposit yield and cash deployment",
            "Design visual analytics telemetry for executive investment reporting"
        ),
        perks = listOf("Performance Bonus (30-50%)", "Executive Healthcare", "Global Relocation"),
        companyEmoji = "📈",
        defaultMediaType = CardMediaType.IMAGE,
        brandColor = DarkYellow,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
    ),
    JobListing(
        id = "job_4",
        title = "Lead Product Designer (Design Systems & Micro-Interactions)",
        company = "Studio Lumina",
        companyHandle = "@lumina_design",
        category = "Design & UX",
        minSalary = 140000,
        maxSalary = 180000,
        location = "Austin, TX / Remote",
        type = "Full Time",
        experienceLevel = "Senior / Lead",
        timeAgo = "1d ago",
        initialApplicantCount = 63,
        skills = listOf("Figma", "Design Systems", "Micro-Interactions", "Motion UX", "Compose Prototyping"),
        description = "Craft tactile, high-craft digital experiences, intricate data visualization interfaces, and responsive interaction design across mobile and desktop surfaces.",
        responsibilities = listOf(
            "Define the next iteration of our dark-mode analytical design language and tokens",
            "Create fluid motion prototypes and slot-machine / odometer interaction standards",
            "Conduct usability labs and partner directly with mobile engineers during implementation"
        ),
        perks = listOf("Flexible Work Hours", "Equipment Grants", "Wellness & Gym Reimbursement"),
        companyEmoji = "✨",
        defaultMediaType = CardMediaType.IMAGE,
        brandColor = PrimaryBrand,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1542744094-3a31f272c490?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1576267423445-b2e0074d68a4?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
    ),
    JobListing(
        id = "job_5",
        title = "Senior Growth Marketing & Marketplace Strategist",
        company = "Global Merchant Exchange",
        companyHandle = "@gmx_market",
        category = "Marketing",
        minSalary = 130000,
        maxSalary = 170000,
        location = "Seattle, WA / Remote",
        type = "Full Time",
        experienceLevel = "Mid-Senior",
        timeAgo = "1d ago",
        initialApplicantCount = 35,
        skills = listOf("Growth Loops", "Paid Acquisition", "Analytics (SQL)", "Conversion Optimization"),
        description = "Scale merchant acquisition, user retention loops, and multi-channel campaign strategies for our global decentralized shopping portal.",
        responsibilities = listOf(
            "Own acquisition KPIs across paid channels, creator partnerships, and referral mechanics",
            "Run structured A/B testing on e-commerce catalog feeds and conversion funnels",
            "Analyze shopping cart abandonment and formulate personalized re-engagement mechanics"
        ),
        perks = listOf("Performance Commission", "Remote First Culture", "Annual Company Offsites"),
        companyEmoji = "🚀",
        defaultMediaType = CardMediaType.VIDEO,
        brandColor = AccentRed,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1556761175-5973dc0f32e7?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1497215728101-856f4ea42174?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4"
    ),
    JobListing(
        id = "job_6",
        title = "Director of Product Management (Analytics & Fintech)",
        company = "Apex Capital Systems",
        companyHandle = "@apex_fintech",
        category = "Product",
        minSalary = 200000,
        maxSalary = 265000,
        location = "San Francisco, CA / Hybrid",
        type = "Full Time",
        experienceLevel = "Director / VP",
        timeAgo = "2d ago",
        initialApplicantCount = 14,
        skills = listOf("Fintech Product Strategy", "Roadmapping", "High-Volume Data", "Executive Leadership"),
        description = "Shape the roadmap for real-time telemetry, interactive web portals, and investment yield management solutions across thousands of active retail and institutional accounts.",
        responsibilities = listOf(
            "Define product vision, strategy, and go-to-market execution for portal modules",
            "Align engineering, design, and regulatory teams to accelerate product releases",
            "Present product velocity and key telemetry milestones to the board"
        ),
        perks = listOf("Executive Stock Options", "Car Allowance", "Comprehensive Family Health"),
        companyEmoji = "🏛️",
        defaultMediaType = CardMediaType.VIDEO,
        brandColor = PrimaryBrand,
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1517048676732-d65bc937f952?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1497366811353-6870744d04b2?w=1200&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1557804506-669a67965ba0?w=1200&auto=format&fit=crop&q=80"
        ),
        videoPreviewUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    )
)
