package com.example.skripsi.data.model

import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val type: QuestionType,
    val question: String,
    val options: List<String>? = null,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("words_to_order") val wordsToOrder: List<String>? = null
)