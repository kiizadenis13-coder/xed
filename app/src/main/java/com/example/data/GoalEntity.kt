package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalCategory {
    INVESTMENT,
    MARKET,
    CAREER,
    GENERAL
}

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: GoalCategory,
    val currentProgress: Float, // e.g. 75f
    val targetProgress: Float,  // e.g. 100f
    val unit: String = "%",     // "%", "$", "courses", "apps"
    val isCompleted: Boolean = false,
    val notes: String = "",
    val dueDate: String = "Q3 Target",
    val createdAt: Long = System.currentTimeMillis()
)
