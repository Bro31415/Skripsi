package com.example.skripsi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chapter (
    val id: Long,
    val name: String,
    @SerialName("created_at")
    val createdAt: String? = null
)