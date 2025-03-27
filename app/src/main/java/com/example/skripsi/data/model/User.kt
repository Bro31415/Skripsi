package com.example.skripsi.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val xp: Int? = null, //jadi nullable
    val created_at: Instant?,
    val user_photo_profile: String? = null
)
