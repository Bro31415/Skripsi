package com.example.skripsi.data.model

import android.os.Parcelable
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Question(
    val id: Long,
    val quizId: Long,
    val questionText: String,
    val questionType: String,
    val answer: String,
    val options: List<String>,
    val xp: Short?,
    val createdAt: String? = null
) : Parcelable