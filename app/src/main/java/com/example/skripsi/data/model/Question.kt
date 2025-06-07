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
    @SerialName("quiz_id")
    val quizId: Long,
    @SerialName("question_text")
    val questionText: String,
    @SerialName("question_type")
    val questionType: String,
    val answer: String,
    val options: List<String>? = emptyList(),
    val xp: Short?,
    @SerialName("created_at")
    val createdAt: String? = null,
    val sentence: String? = null
) : Parcelable