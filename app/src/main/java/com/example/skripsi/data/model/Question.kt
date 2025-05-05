package com.example.skripsi.data.model

import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Long,
    val quizId: Long,
    val questionText: String,
    val questionType: String,
    val answer: String,
    val xp: Short?,
    val createdAt: String
)