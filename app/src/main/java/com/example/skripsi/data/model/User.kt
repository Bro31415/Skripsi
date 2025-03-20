package com.example.skripsi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val xp: Int? = null // Kolom xp diizinkan bernilai null (tambahan wil)
)
