package com.example.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class AnalyticsRepository(private val goalDao: GoalDao) {

    val allGoals: Flow<List<GoalEntity>> = goalDao.getAllGoals()

    suspend fun insertGoal(goal: GoalEntity): Long = goalDao.insertGoal(goal)

    suspend fun updateGoal(goal: GoalEntity) = goalDao.updateGoal(goal)

    suspend fun deleteGoal(goal: GoalEntity) = goalDao.deleteGoal(goal)

    suspend fun updateGoalProgress(id: Long, progress: Float, isCompleted: Boolean) =
        goalDao.updateProgress(id, progress, isCompleted)

    /**
     * Real-time stream emitter providing live ticks for portfolio valuation, market sentiment,
     * and real-time activity events.
     */
    fun createRealtimeStream(intervalMs: Long = 2000L): Flow<RealtimeTickUpdate> = flow {
        var currentPortfolio = 148520.45
        var currentSentiment = 78
        var currentMastery = 84.5f

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val eventTemplates = listOf(
            Pair("S&P 500 Spike", "Tech sector momentum pushes index +0.65%"),
            Pair("Dividend Reinvested", "Automated DRIP triggered for Global ETF +$48.20"),
            Pair("Job Market Alert", "14 new Staff Android Engineer positions listed"),
            Pair("Career XP Gained", "+150 XP for completing Market Analytics review"),
            Pair("Crypto Arbitrage Tick", "BTC/ETH ratio dynamically adjusted by +0.32%"),
            Pair("Portfolio Milestone", "Asset allocation reached optimal 98% efficiency balance"),
            Pair("Vol Index Shift", "CBOE VIX compressed to 13.8 - low volatility environment")
        )

        while (true) {
            delay(intervalMs)
            val deltaPct = (Random.nextDouble(-0.4, 0.65))
            val deltaVal = currentPortfolio * (deltaPct / 100.0)
            currentPortfolio = (currentPortfolio + deltaVal).coerceAtLeast(100000.0)

            val sentimentDelta = Random.nextInt(-2, 3)
            currentSentiment = (currentSentiment + sentimentDelta).coerceIn(40, 95)

            val masteryDelta = if (Random.nextBoolean()) 0.1f else 0.0f
            currentMastery = (currentMastery + masteryDelta).coerceAtMost(100f)

            val isEventTick = Random.nextInt(0, 3) == 0
            val event = if (isEventTick) {
                val template = eventTemplates.random()
                LiveActivityEvent(
                    id = System.currentTimeMillis().toString(),
                    title = template.first,
                    detail = template.second,
                    timestamp = timeFormat.format(Date()),
                    type = when {
                        template.first.contains("Job") -> EventType.CAREER_ACHIEVEMENT
                        template.first.contains("Portfolio") || template.first.contains("Dividend") -> EventType.INVESTMENT
                        else -> EventType.MARKET_TICK
                    },
                    delta = if (deltaPct >= 0) "+%.2f%%".format(deltaPct) else "%.2f%%".format(deltaPct)
                )
            } else null

            emit(
                RealtimeTickUpdate(
                    portfolioValue = currentPortfolio,
                    portfolioDeltaPercent = deltaPct,
                    marketSentimentScore = currentSentiment,
                    careerMasteryScore = currentMastery,
                    newActivity = event,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}

data class RealtimeTickUpdate(
    val portfolioValue: Double,
    val portfolioDeltaPercent: Double,
    val marketSentimentScore: Int,
    val careerMasteryScore: Float,
    val newActivity: LiveActivityEvent?,
    val timestamp: Long
)
