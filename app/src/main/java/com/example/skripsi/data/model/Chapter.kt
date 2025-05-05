package com.example.skripsi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Chapter (
    val id: Long,
    val name: String,
    val createdAt: String
)