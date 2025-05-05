package com.example.skripsi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz (
    val id: Long,
    val chapterId: Long,
    val quizType: String,
    val createdAt: String
)