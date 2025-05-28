package com.example.skripsi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserQuizAttempt (
    val id: Long? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("quiz_id")
    val quizId: Long,
    @SerialName("total_questions")
    val totalQuestions: Int?,
    @SerialName("xp_earned")
    val xpEarned: Int?,
    @SerialName("quiz_duration")
    val quizDuration: Int,
    @SerialName("started_at")
    val startedAt: String?,
    @SerialName("finished_at")
    val finishedAt: String
)