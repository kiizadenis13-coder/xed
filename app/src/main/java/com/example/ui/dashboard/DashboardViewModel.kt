package com.example.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AccountOrderDeliveryManager
import com.example.data.AppDatabase
import com.example.data.AnalyticsRepository
import com.example.data.AuthUser
import com.example.data.ChatMessage
import com.example.data.ContactsChatRepository
import com.example.data.GoalCategory
import com.example.data.GoalEntity
import com.example.data.LiveActivityEvent
import com.example.data.MarketDeliveryOrder
import com.example.data.MetricPoint
import com.example.data.PhoneContact
import com.example.data.PortalRepository
import com.example.data.RealtimeAnalyticsState
import com.example.data.UserAccountPost
import com.example.data.UserAccountType
import com.example.portals.PortalType
import com.example.ui.components.ProductItem
import com.example.ui.components.TimeRange
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AnalyticsRepository(database.goalDao())
    private val contactsRepository = ContactsChatRepository(application.applicationContext, viewModelScope)
    private val portalRepository = PortalRepository(database.portalCacheDao(), viewModelScope)
    val deliveryManager = AccountOrderDeliveryManager(viewModelScope)
    val userPosts: StateFlow<List<UserAccountPost>> = deliveryManager.userPosts
    val deliveryOrders: StateFlow<List<MarketDeliveryOrder>> = deliveryManager.orders

    // Room Database Caching for Portal and Market Data (100% Offline Functional)
    val cachedMarketProducts: StateFlow<List<ProductItem>> = portalRepository.cachedMarketProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PortalRepository.getInitialDefaultProducts()
        )
    val isRoomCacheReady: StateFlow<Boolean> = portalRepository.isOfflineCacheReady

    fun recordProductPurchase(productId: String) {
        viewModelScope.launch {
            portalRepository.recordProductPurchase(productId)
        }
    }

    // User Authentication State
    private val _authUser = MutableStateFlow(
        AuthUser(
            id = "usr_001",
            name = "Denis Kiiza",
            email = "kiizadenis13@gmail.com",
            phone = "+1 (555) 389-4021",
            avatarEmoji = "👨‍💻",
            avatarColorHex = 0xFF4F46E5,
            isLoggedIn = true,
            accountType = "Executive Pro"
        )
    )
    val authUser: StateFlow<AuthUser> = _authUser.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    // Contacts & Chat State
    val contacts: StateFlow<List<PhoneContact>> = contactsRepository.contacts
    val messagesMap: StateFlow<Map<String, List<ChatMessage>>> = contactsRepository.messagesMap
    val hasPhonePermission: StateFlow<Boolean> = contactsRepository.hasPhonePermission

    private val _activeChatContact = MutableStateFlow<PhoneContact?>(null)
    val activeChatContact: StateFlow<PhoneContact?> = _activeChatContact.asStateFlow()

    val goals: StateFlow<List<GoalEntity>> = repository.allGoals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _analyticsState = MutableStateFlow(
        RealtimeAnalyticsState(
            portfolioValue = 148520.45,
            portfolioDeltaPercent = 4.82,
            marketSentimentScore = 78,
            careerMasteryScore = 84.5f,
            activeGoalVelocity = 91.0f,
            livePoints = generateInitialChartPoints(TimeRange.ONE_DAY)
        )
    )
    val analyticsState: StateFlow<RealtimeAnalyticsState> = _analyticsState.asStateFlow()

    private val _selectedTimeRange = MutableStateFlow(TimeRange.ONE_DAY)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange.asStateFlow()

    private val _isSidebarExpanded = MutableStateFlow(false)
    val isSidebarExpanded: StateFlow<Boolean> = _isSidebarExpanded.asStateFlow()

    private val _activeModalPortal = MutableStateFlow<PortalType?>(null)
    val activeModalPortal: StateFlow<PortalType?> = _activeModalPortal.asStateFlow()

    private val _showAddGoalDialog = MutableStateFlow(false)
    val showAddGoalDialog: StateFlow<Boolean> = _showAddGoalDialog.asStateFlow()

    private var streamJob: Job? = null

    init {
        startRealtimeStream()
    }

    private fun startRealtimeStream() {
        streamJob?.cancel()
        streamJob = viewModelScope.launch {
            repository.createRealtimeStream(intervalMs = 2000L).collect { tick ->
                if (_analyticsState.value.activeStreamTicking) {
                    _analyticsState.update { current ->
                        val updatedPoints = (current.livePoints + MetricPoint(
                            timestamp = tick.timestamp,
                            value = tick.portfolioValue,
                            label = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(tick.timestamp))
                        )).takeLast(24) // Keep sliding window

                        val updatedFeed = if (tick.newActivity != null) {
                            listOf(tick.newActivity) + current.activityStream.take(15)
                        } else current.activityStream

                        current.copy(
                            portfolioValue = tick.portfolioValue,
                            portfolioDeltaPercent = tick.portfolioDeltaPercent,
                            marketSentimentScore = tick.marketSentimentScore,
                            careerMasteryScore = tick.careerMasteryScore,
                            livePoints = updatedPoints,
                            activityStream = updatedFeed
                        )
                    }
                }
            }
        }
    }

    fun toggleStreamTicking() {
        _analyticsState.update { it.copy(activeStreamTicking = !it.activeStreamTicking) }
    }

    fun selectTimeRange(range: TimeRange) {
        _selectedTimeRange.value = range
        _analyticsState.update { current ->
            current.copy(livePoints = generateInitialChartPoints(range, current.portfolioValue))
        }
    }

    fun toggleSidebarExpanded() {
        _isSidebarExpanded.update { !it }
    }

    fun setSidebarExpanded(expanded: Boolean) {
        _isSidebarExpanded.value = expanded
    }

    fun openPortalModal(portal: PortalType) {
        _activeModalPortal.value = portal
    }

    fun closePortalModal() {
        _activeModalPortal.value = null
    }

    fun setShowAddGoalDialog(show: Boolean) {
        _showAddGoalDialog.value = show
    }

    fun addGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.insertGoal(goal)
            _showAddGoalDialog.value = false
        }
    }

    fun updateGoalProgress(goal: GoalEntity, delta: Float) {
        viewModelScope.launch {
            val newProgress = (goal.currentProgress + delta).coerceIn(0f, 100f)
            val isCompleted = newProgress >= 100f
            repository.updateGoalProgress(goal.id, newProgress, isCompleted)
        }
    }

    fun toggleGoalCompleted(goal: GoalEntity) {
        viewModelScope.launch {
            val newCompleted = !goal.isCompleted
            val newProgress = if (newCompleted) 100f else 50f
            repository.updateGoalProgress(goal.id, newProgress, newCompleted)
        }
    }

    fun setShowAuthDialog(show: Boolean) {
        _showAuthDialog.value = show
    }

    fun login(email: String, name: String) {
        _authUser.value = _authUser.value.copy(
            email = email,
            name = name,
            isLoggedIn = true
        )
        _showAuthDialog.value = false
    }

    fun logout() {
        _authUser.value = _authUser.value.copy(
            isLoggedIn = false
        )
    }

    fun switchAccountType(type: UserAccountType) {
        _authUser.update { it.copy(activeAccountType = type) }
    }

    fun depositWallet(amount: Double) {
        _authUser.update { it.copy(walletBalance = it.walletBalance + amount) }
    }

    fun createPost(
        title: String,
        description: String,
        priceOrRate: Double,
        priceLabel: String,
        category: String,
        photoUrl: String
    ) {
        val user = _authUser.value
        deliveryManager.publishAccountPost(
            accountType = user.activeAccountType,
            authorName = user.name,
            authorPhotoUrl = user.avatarPhotoUrl,
            title = title,
            description = description,
            priceOrRate = priceOrRate,
            priceLabel = priceLabel,
            category = category,
            photoUrl = photoUrl
        )
    }

    data class ActiveProductCheckout(
        val productId: String,
        val title: String,
        val price: Double,
        val photoUrl: String,
        val sellerName: String
    )

    private val _activeProductCheckout = MutableStateFlow<ActiveProductCheckout?>(null)
    val activeProductCheckout: StateFlow<ActiveProductCheckout?> = _activeProductCheckout.asStateFlow()

    fun openProductCheckout(
        productId: String,
        title: String,
        price: Double,
        photoUrl: String,
        sellerName: String
    ) {
        _activeProductCheckout.value = ActiveProductCheckout(productId, title, price, photoUrl, sellerName)
    }

    fun closeProductCheckout() {
        _activeProductCheckout.value = null
    }

    fun openChat(contact: PhoneContact) {
        _activeChatContact.value = contact
    }

    fun closeChat() {
        _activeChatContact.value = null
    }

    fun sendMessage(contactId: String, text: String) {
        contactsRepository.sendMessage(contactId, text)
    }

    fun requestPhoneContactsSync() {
        contactsRepository.setPhonePermissionGranted(true)
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    private fun generateInitialChartPoints(timeRange: TimeRange, baseValue: Double = 148520.45): List<MetricPoint> {
        val points = mutableListOf<MetricPoint>()
        val count = 20
        var current = baseValue * 0.94
        val now = System.currentTimeMillis()
        val stepMs = when (timeRange) {
            TimeRange.ONE_HOUR -> 3 * 60 * 1000L
            TimeRange.ONE_DAY -> 72 * 60 * 1000L
            TimeRange.ONE_WEEK -> 8 * 3600 * 1000L
            TimeRange.ONE_MONTH -> 36 * 3600 * 1000L
            TimeRange.ONE_YEAR -> 18 * 24 * 3600 * 1000L
        }

        val dateFormat = when (timeRange) {
            TimeRange.ONE_HOUR, TimeRange.ONE_DAY -> SimpleDateFormat("HH:mm", Locale.getDefault())
            TimeRange.ONE_WEEK, TimeRange.ONE_MONTH -> SimpleDateFormat("MM/dd", Locale.getDefault())
            TimeRange.ONE_YEAR -> SimpleDateFormat("MMM", Locale.getDefault())
        }

        for (i in count downTo 0) {
            val time = now - (i * stepMs)
            current += (Random.nextDouble(-600.0, 950.0))
            points.add(
                MetricPoint(
                    timestamp = time,
                    value = current.coerceAtLeast(100000.0),
                    label = dateFormat.format(Date(time))
                )
            )
        }
        return points
    }
}
