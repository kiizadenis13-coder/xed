package com.example.data

data class MetricPoint(
    val timestamp: Long,
    val value: Double,
    val label: String
)

data class LiveActivityEvent(
    val id: String,
    val title: String,
    val detail: String,
    val timestamp: String,
    val type: EventType,
    val delta: String? = null
)

enum class EventType {
    INVESTMENT,
    MARKET_TICK,
    CAREER_ACHIEVEMENT,
    SYSTEM_ALERT
}

data class RealtimeAnalyticsState(
    val portfolioValue: Double = 148520.45,
    val portfolioDeltaPercent: Double = 4.82,
    val marketSentimentScore: Int = 78, // 0 - 100 Greed / Fear
    val marketSentimentLabel: String = "Greed (Bullish)",
    val careerMasteryScore: Float = 84.5f,
    val careerMasteryDelta: Float = 6.2f,
    val activeGoalVelocity: Float = 91.0f,
    val activeStreamTicking: Boolean = true,
    val tickFrequencySeconds: Int = 2,
    val livePoints: List<MetricPoint> = emptyList(),
    val activityStream: List<LiveActivityEvent> = emptyList()
)
