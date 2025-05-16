package com.example.skripsi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class QuestionType {
    multiple_choice,
    //@SerialName("multiple_choice") MULTIPLE_CHOICE,
    @SerialName("fill_in_the_blank") FILL_IN_THE_BLANK,
    @SerialName("match") MATCH
}