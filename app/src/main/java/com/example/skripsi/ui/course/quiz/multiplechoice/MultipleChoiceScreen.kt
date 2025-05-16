package com.example.skripsi.ui.course.quiz.multiplechoice

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skripsi.ui.quiz.multiplechoice.MultipleChoiceViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun MultipleChoiceContent(viewModel: MultipleChoiceViewModel) {
    val questions by viewModel.questions.collectAsState()

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        var currentIndex by remember { mutableStateOf(0) }
        var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }
        var showAnswerFeedback by remember { mutableStateOf(false) }

        val question = questions[currentIndex]
        val selectedOption = selectedAnswers[question.id]
        val totalQuestions = questions.size

        val score = selectedAnswers.count { (id, answer) ->
            val q = questions.find { it.id == id }
            q?.correctAnswer == answer
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* logic balik */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1f) / totalQuestions },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(6.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Text("${currentIndex + 1} dari $totalQuestions")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Pilih jawaban yang sesuai!", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = question.text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Options -> 2-column grid
            Column {
                question.options.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowOptions.forEach { option ->
                            val isCorrect = option == question.correctAnswer
                            val isSelected = selectedOption == option

                            val bgColor = when {
                                showAnswerFeedback && isSelected && isCorrect -> Color(0xFFD0F5D6) // greenish
                                showAnswerFeedback && isSelected && !isCorrect -> Color(0xFFFFD6D6) // reddish
                                else -> Color.Transparent
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedAnswers[question.id] = option
                                    showAnswerFeedback = true
                                },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = bgColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .defaultMinSize(minHeight = 64.dp) // memberi tinggi minimum
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    maxLines = 2, // agar teks bisa turun jika panjang
                                    softWrap = true
                                )
                            }

                        }
                        if (rowOptions.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Navigation buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            showAnswerFeedback = selectedAnswers.containsKey(questions[currentIndex].id)
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentIndex < totalQuestions - 1) {
                            currentIndex++
                            showAnswerFeedback = selectedAnswers.containsKey(questions[currentIndex].id)
                        }
                    },
                    enabled = currentIndex < totalQuestions - 1
                ) {
                    Text("Next")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Skor: $score / $totalQuestions",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}



// Function to use inside Fragment
fun MultipleChoiceScreen(context: Context, viewModel: MultipleChoiceViewModel): ComposeView {
    return ComposeView(context).apply {
        setContent {
            MaterialTheme {
                MultipleChoiceContent(viewModel)
            }
        }
    }
}
