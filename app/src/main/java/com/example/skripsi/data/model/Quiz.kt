package com.example.skripsi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quiz (
    val id: Long,
    @SerialName("chapter_id")
    val chapterId: Long,
    @SerialName("quiz_type")
    val quizType: String,
    val createdAt: String? = null
)