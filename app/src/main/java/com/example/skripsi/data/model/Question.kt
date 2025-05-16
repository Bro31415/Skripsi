package com.example.skripsi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Question(
    @SerialName("id")
    val id: Int,

    @SerialName("quiz_id")
    val quizId: Int,

    @SerialName("question_text")
    val text: String,

    @SerialName("question_type")
    val type: QuestionType,

    @SerialName("options")
    val options: List<String>,

    @SerialName("answer")
    val correctAnswer: String,

    @SerialName("xp")
    val xp: Int,

    @SerialName("created_at")
    val createdAt: String
) : Parcelable