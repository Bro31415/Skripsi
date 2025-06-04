package com.example.skripsi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dictionary(
    val id: Long,
    @SerialName("chapter_id")
    val chapterId: Long,
    val word: String,
    val definition: String,
    val example: String,
    val translation: String,
    @SerialName("created_at")
    val createdAt: String? = null
)
