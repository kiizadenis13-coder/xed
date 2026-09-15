package com.example.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class AuthUser(
    val id: String = "usr_001",
    val name: String = "Denis Kiiza",
    val email: String = "kiizadenis13@gmail.com",
    val phone: String = "+1 (555) 389-4021",
    val avatarEmoji: String = "👨‍💻",
    val avatarPhotoUrl: String = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
    val avatarColorHex: Long = 0xFF10B981,
    val isLoggedIn: Boolean = false,
    val accountType: String = "Executive Pro",
    val activeAccountType: UserAccountType = UserAccountType.SELLER_ACCOUNT,
    val walletBalance: Double = 120.0
)

data class PhoneContact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val avatarColorHex: Long,
    val avatarEmoji: String = "👤",
    val photoUrl: String? = null,
    val isOnline: Boolean = true,
    val isFromPhone: Boolean = false,
    val roleOrCompany: String = "",
    val lastMessageSnippet: String = "",
    val lastMessageTimestamp: Long = 0L,
    val unreadCount: Int = 0
) {
    val formattedTime: String
        get() {
            if (lastMessageTimestamp <= 0L) return "Recent"
            val now = System.currentTimeMillis()
            val diff = now - lastMessageTimestamp
            return when {
                diff < 60_000L -> "Just now"
                diff < 3600_000L -> "${diff / 60_000L}m ago"
                diff < 86400_000L -> "${diff / 3600_000L}h ago"
                else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(lastMessageTimestamp))
            }
        }
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val contactId: String,
    val text: String,
    val isFromMe: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val formattedTime: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
)

class ContactsChatRepository(private val context: Context, private val coroutineScope: CoroutineScope) {

    private val now = System.currentTimeMillis()

    // Curated initial seed contacts with rich conversation history and authentic portraits
    private val initialContacts = listOf(
        PhoneContact(
            id = "c_sarah",
            name = "Sarah Jenkins",
            phoneNumber = "+1 (415) 890-2104",
            email = "sarah.j@meridian-ventures.com",
            avatarColorHex = 0xFF10B981, // Emerald
            avatarEmoji = "👩‍💼",
            photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80",
            isOnline = true,
            roleOrCompany = "Lead Portfolio Partner",
            lastMessageSnippet = "The Q3 NAV metrics look solid! Let's lock in the yield rebalancing today.",
            lastMessageTimestamp = now - (3 * 60 * 1000L), // 3 mins ago - MOST RECENT!
            unreadCount = 1
        ),
        PhoneContact(
            id = "c_alex",
            name = "Alex Rivera",
            phoneNumber = "+1 (650) 441-9230",
            email = "alex.r@apextech.io",
            avatarColorHex = 0xFF059669, // Forest Emerald
            avatarEmoji = "👨‍💻",
            photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
            isOnline = true,
            roleOrCompany = "Principal AI Engineer",
            lastMessageSnippet = "Autonomous pipeline deployment completed with 0 latency drift.",
            lastMessageTimestamp = now - (28 * 60 * 1000L), // 28 mins ago
            unreadCount = 0
        ),
        PhoneContact(
            id = "c_elena",
            name = "Elena Rostova",
            phoneNumber = "+1 (212) 773-5182",
            email = "elena@vanguardmarket.com",
            avatarColorHex = 0xFF34D399, // Mint
            avatarEmoji = "👩‍🎨",
            photoUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=300&q=80",
            isOnline = false,
            roleOrCompany = "Market Operations Lead",
            lastMessageSnippet = "Bulk order logistics confirmed. All tracking IDs are updated.",
            lastMessageTimestamp = now - (2 * 3600 * 1000L), // 2 hrs ago
            unreadCount = 2
        ),
        PhoneContact(
            id = "c_marcus",
            name = "Marcus Vance",
            phoneNumber = "+1 (312) 505-8819",
            email = "m.vance@talenthub.global",
            avatarColorHex = 0xFF10B981, // Emerald
            avatarEmoji = "👨‍💼",
            photoUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=300&q=80",
            isOnline = true,
            roleOrCompany = "Senior Executive Recruiter",
            lastMessageSnippet = "We received 14 qualified candidate applications for the AI Staff role.",
            lastMessageTimestamp = now - (6 * 3600 * 1000L), // 6 hrs ago
            unreadCount = 0
        ),
        PhoneContact(
            id = "c_devon",
            name = "Devon Miles",
            phoneNumber = "+1 (206) 880-1493",
            email = "devon.m@cloudscale.net",
            avatarColorHex = 0xFF047857, // Deep Emerald
            avatarEmoji = "🧑‍💻",
            photoUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=300&q=80",
            isOnline = true,
            roleOrCompany = "Infrastructure Architect",
            lastMessageSnippet = "Server cluster auto-scaling thresholds tuned for peak volume.",
            lastMessageTimestamp = now - (14 * 3600 * 1000L), // 14 hrs ago
            unreadCount = 0
        ),
        PhoneContact(
            id = "c_maya",
            name = "Maya Lin",
            phoneNumber = "+1 (408) 612-9045",
            email = "maya.lin@uxstudio.design",
            avatarColorHex = 0xFF6EE7B7, // Light Emerald
            avatarEmoji = "👩‍💻",
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
            isOnline = false,
            roleOrCompany = "Lead UX Designer",
            lastMessageSnippet = "Exported the high-res mockups and interactive design tokens.",
            lastMessageTimestamp = now - (24 * 3600 * 1000L), // 1 day ago
            unreadCount = 0
        )
    )

    private val initialMessages = mapOf(
        "c_sarah" to listOf(
            ChatMessage(contactId = "c_sarah", text = "Good morning! Did you inspect the updated yield models?", isFromMe = false, timestamp = now - 600000L),
            ChatMessage(contactId = "c_sarah", text = "Yes, reviewed them. The +4.82% delta looks remarkable.", isFromMe = true, timestamp = now - 300000L),
            ChatMessage(contactId = "c_sarah", text = "The Q3 NAV metrics look solid! Let's lock in the yield rebalancing today.", isFromMe = false, timestamp = now - 180000L)
        ),
        "c_alex" to listOf(
            ChatMessage(contactId = "c_alex", text = "Hey, running the integration unit tests right now.", isFromMe = false, timestamp = now - 2000000L),
            ChatMessage(contactId = "c_alex", text = "Keep me posted once the CI pipeline goes green.", isFromMe = true, timestamp = now - 1900000L),
            ChatMessage(contactId = "c_alex", text = "Autonomous pipeline deployment completed with 0 latency drift.", isFromMe = false, timestamp = now - 1680000L)
        ),
        "c_elena" to listOf(
            ChatMessage(contactId = "c_elena", text = "Bulk order logistics confirmed. All tracking IDs are updated.", isFromMe = false, timestamp = now - 7200000L)
        ),
        "c_marcus" to listOf(
            ChatMessage(contactId = "c_marcus", text = "We received 14 qualified candidate applications for the AI Staff role.", isFromMe = false, timestamp = now - 21600000L)
        )
    )

    private val _contacts = MutableStateFlow<List<PhoneContact>>(initialContacts.sortedByDescending { it.lastMessageTimestamp })
    val contacts: StateFlow<List<PhoneContact>> = _contacts.asStateFlow()

    private val _messagesMap = MutableStateFlow<Map<String, List<ChatMessage>>>(initialMessages)
    val messagesMap: StateFlow<Map<String, List<ChatMessage>>> = _messagesMap.asStateFlow()

    private val _hasPhonePermission = MutableStateFlow(
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    )
    val hasPhonePermission: StateFlow<Boolean> = _hasPhonePermission.asStateFlow()

    init {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            fetchPhoneContacts()
        }
    }

    fun setPhonePermissionGranted(granted: Boolean) {
        _hasPhonePermission.value = granted
        if (granted) {
            fetchPhoneContacts()
        }
    }

    fun fetchPhoneContacts() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val phoneContactsList = mutableListOf<PhoneContact>()
                val cursor: Cursor? = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                )

                cursor?.use {
                    val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    val seenNumbers = mutableSetOf<String>()
                    val avatarColors = listOf(0xFF6366F1, 0xFF10B981, 0xFFEC4899, 0xFFF59E0B, 0xFF8B5CF6, 0xFF06B6D4, 0xFF3B82F6)
                    val avatarEmojis = listOf("👤", "📱", "💼", "✨", "🤝", "🌟", "👨‍💻", "👩‍💼")
                    var colorIdx = 0

                    while (it.moveToNext() && phoneContactsList.size < 150) {
                        val id = if (idIndex != -1) it.getString(idIndex) else UUID.randomUUID().toString()
                        val name = if (nameIndex != -1) it.getString(nameIndex) ?: "Contact" else "Contact"
                        val number = if (numberIndex != -1) it.getString(numberIndex) ?: "" else ""

                        val normalized = number.replace(Regex("[^0-9+]"), "")
                        if (normalized.isNotBlank() && !seenNumbers.contains(normalized)) {
                            seenNumbers.add(normalized)
                            phoneContactsList.add(
                                PhoneContact(
                                    id = "phone_$id",
                                    name = name,
                                    phoneNumber = number,
                                    email = "",
                                    avatarColorHex = avatarColors[colorIdx % avatarColors.size],
                                    avatarEmoji = avatarEmojis[colorIdx % avatarEmojis.size],
                                    isOnline = (colorIdx % 2 == 0),
                                    isFromPhone = true,
                                    roleOrCompany = "Phone Contact",
                                    lastMessageSnippet = "Tap to start conversation with $name",
                                    lastMessageTimestamp = 0L,
                                    unreadCount = 0
                                )
                            )
                            colorIdx++
                        }
                    }
                }

                if (phoneContactsList.isNotEmpty()) {
                    _contacts.update { current ->
                        // Merge phone contacts with current contacts, avoiding duplication
                        val existingIds = current.map { it.phoneNumber.replace(Regex("[^0-9]"), "") }.toSet()
                        val newPhoneContacts = phoneContactsList.filter {
                            !existingIds.contains(it.phoneNumber.replace(Regex("[^0-9]"), ""))
                        }
                        (current + newPhoneContacts).sortedByDescending { it.lastMessageTimestamp }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(contactId: String, text: String) {
        if (text.isBlank()) return
        val messageTime = System.currentTimeMillis()
        val userMessage = ChatMessage(
            contactId = contactId,
            text = text.trim(),
            isFromMe = true,
            timestamp = messageTime
        )

        // 1. Add user message
        _messagesMap.update { current ->
            val existing = current[contactId] ?: emptyList()
            current + (contactId to (existing + userMessage))
        }

        // 2. Update contact's lastMessageSnippet and lastMessageTimestamp to now!
        // This IMMEDIATELY bumps this contact card to the very top spot!
        _contacts.update { list ->
            list.map { contact ->
                if (contact.id == contactId) {
                    contact.copy(
                        lastMessageSnippet = "You: ${text.trim()}",
                        lastMessageTimestamp = messageTime,
                        unreadCount = 0
                    )
                } else contact
            }.sortedByDescending { it.lastMessageTimestamp }
        }

        // 3. Simulated intelligent reply after short delay
        coroutineScope.launch {
            delay(1200)
            val replyTime = System.currentTimeMillis()
            val replyText = generateSimulatedReply(text)
            val replyMessage = ChatMessage(
                contactId = contactId,
                text = replyText,
                isFromMe = false,
                timestamp = replyTime
            )

            _messagesMap.update { current ->
                val existing = current[contactId] ?: emptyList()
                current + (contactId to (existing + replyMessage))
            }

            // Update contact with the incoming reply, keeping it in the top spot!
            _contacts.update { list ->
                list.map { contact ->
                    if (contact.id == contactId) {
                        contact.copy(
                            lastMessageSnippet = replyText,
                            lastMessageTimestamp = replyTime,
                            unreadCount = 0
                        )
                    } else contact
                }.sortedByDescending { it.lastMessageTimestamp }
            }
        }
    }

    private fun generateSimulatedReply(userText: String): String {
        val lower = userText.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
                "Hey! Great to connect. Let me know what you'd like to coordinate."
            lower.contains("nav") || lower.contains("investment") || lower.contains("yield") ->
                "The portfolio models are live. Yield rate is trending +4.82% as scheduled!"
            lower.contains("market") || lower.contains("order") || lower.contains("buy") ->
                "Order status received and synced across merchant hubs."
            lower.contains("job") || lower.contains("interview") || lower.contains("hire") ->
                "Candidate profiles and interview scheduling links are updated in the portal."
            lower.contains("meeting") || lower.contains("call") || lower.contains("sync") ->
                "Sounds great, I've cleared time on my calendar. Send the invite over!"
            lower.contains("thanks") || lower.contains("thank you") ->
                "Anytime! Glad we're moving fast on this."
            else ->
                "Got it! I'm reviewing the details right now and will follow up shortly."
        }
    }
}
