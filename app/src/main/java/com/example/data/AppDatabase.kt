package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        GoalEntity::class,
        PortalCacheEntity::class,
        MarketProductEntity::class,
        PortalCarouselMediaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun portalCacheDao(): PortalCacheDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "analytics_dashboard.db"
                ).addCallback(AppDatabaseCallback(scope))
                 .fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.goalDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: GoalDao) {
                val initialGoals = listOf(
                    GoalEntity(
                        title = "Portfolio Diversification Target (Tech & Green Energy)",
                        category = GoalCategory.INVESTMENT,
                        currentProgress = 82f,
                        targetProgress = 100f,
                        unit = "%",
                        notes = "Rebalance ETF holdings towards 35% growth, 30% dividend yield.",
                        dueDate = "End of Month"
                    ),
                    GoalEntity(
                        title = "Market Intelligence: Master Technical Chart Patterns",
                        category = GoalCategory.MARKET,
                        currentProgress = 65f,
                        targetProgress = 100f,
                        unit = "modules",
                        notes = "Complete RSI divergence & Fibonacci retracement deep dive.",
                        dueDate = "Next Week"
                    ),
                    GoalEntity(
                        title = "Senior Cloud Architect Career Certification",
                        category = GoalCategory.CAREER,
                        currentProgress = 90f,
                        targetProgress = 100f,
                        unit = "%",
                        notes = "Final mock exam passed with 94% score. Scheduled for certification.",
                        dueDate = "Oct 15"
                    ),
                    GoalEntity(
                        title = "Emergency Fund & Liquid Reserve Ratio",
                        category = GoalCategory.INVESTMENT,
                        currentProgress = 100f,
                        targetProgress = 100f,
                        unit = "%",
                        isCompleted = true,
                        notes = "6 months living expenses stored in high-yield cash account.",
                        dueDate = "Achieved"
                    ),
                    GoalEntity(
                        title = "Resume & Portfolio Optimization for High-Growth Roles",
                        category = GoalCategory.CAREER,
                        currentProgress = 75f,
                        targetProgress = 100f,
                        unit = "%",
                        notes = "Tailored CV for Principal Engineer & Fintech Director positions.",
                        dueDate = "In Progress"
                    )
                )
                dao.insertAll(initialGoals)
            }
        }
    }
}
