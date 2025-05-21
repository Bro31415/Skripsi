package com.example.skripsi.data.model

data class UserQuizAttempt (
    val id: Long? = null,
    val userId: String,
    val quizId: Long,
    val score: Int?,
    val totalQuestions: Int?,
    val xpEarned: Int?,
    val quizDuration: Int,
    val startedAt: String?,
    val finishedAt: String
)