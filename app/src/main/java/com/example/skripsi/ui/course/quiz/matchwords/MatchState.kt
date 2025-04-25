package com.example.skripsi.ui.course.quiz.matchwords

data class MatchState(
    val availableWords: List<String> = emptyList(),
    val droppedWords: List<String> = emptyList(),
    val matchedWords: List<String> = emptyList(),
    val draggedWord: String? = null,
    val isCheckingAnswers: Boolean = false,
    val isCorrect: Boolean? = null
)
