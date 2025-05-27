package com.example.skripsi.ui.course.quiz.multiplechoice

import androidx.compose.foundation.background
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
import com.example.skripsi.ui.course.QuizRunnerActivity

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

    var showResult by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val activity = context as? QuizRunnerActivity

    LaunchedEffect(showResult) {
        if (showResult) {
            kotlinx.coroutines.delay(3000) // 3 detik
            activity?.continueQuestion()
        }
    }

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
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

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

        if (!showResult) {
            Button(
                onClick = {
                    if (selected != null) {
                        showResult = true
                    }
                },
                enabled = selected != null
            ) {
                Text("Submit")
            }
        } else {
            val feedbackColor = if (isCorrect == true)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer

            val feedbackTextColor = if (isCorrect == true)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onErrorContainer

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp)
                    .background(color = feedbackColor, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCorrect == true) "Benar!" else "Salah!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = feedbackTextColor
                )
            }
        }
    }
}


