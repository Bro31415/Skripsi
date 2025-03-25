package com.example.skripsi.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val xp: Int = 0 ,
    val created_at: Instant?,
)
