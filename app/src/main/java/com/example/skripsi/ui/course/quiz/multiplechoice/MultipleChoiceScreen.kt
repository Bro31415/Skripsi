package com.example.skripsi.ui.course.quiz.multiplechoice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skripsi.data.model.Question
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

@Composable
fun MultipleChoiceScreen(
    question: Question,
    savedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    // Buat ViewModel dengan question lewat factory
    val factory = MultipleChoiceViewModelFactory(question, savedAnswer, onAnswerSelected)
    val vm: MultipleChoiceViewModel = viewModel(factory = factory)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MultipleChoiceContent(vm)
    }
}

@Composable
fun MultipleChoiceContent(viewModel: MultipleChoiceViewModel) {
    val questionText = viewModel.questionText
    val options = viewModel.options
    val selected = viewModel.selectedAnswer
    val isCorrect = viewModel.isAnswerCorrect

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer sebelum pertanyaan (biar tidak terlalu atas)
        Spacer(modifier = Modifier.height(16.dp))

        // Teks soal
        Text(
            text = questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Opsi dalam dua kolom
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { opt ->
                        val bgColor = when {
                            isCorrect == true && selected == opt ->
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            isCorrect == false && selected == opt ->
                                MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        OutlinedButton(
                            onClick = { viewModel.selectAnswer(opt) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = bgColor
                            )
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2
                            )
                        }
                    }

                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

