package com.example.skripsi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skripsi.viewmodel.FillInTheBlankViewModel

@Composable
fun FillInTheBlankScreen(
    viewModel: FillInTheBlankViewModel,
    onResultValidated: (Boolean) -> Unit
) {
    val selectedAnswer = viewModel.selectedAnswer
    val isAnswerCorrect = viewModel.isAnswerCorrect

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lengkapi kalimat di bawah!",
            style = MaterialTheme.typography.titleMedium
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val sentence = viewModel.question.questionText
            val parts = sentence.split("_____")
            Row {
                Text(text = parts.getOrNull(0) ?: "", fontWeight = FontWeight.Bold)
                Text(text = " _______", modifier = Modifier.padding(horizontal = 4.dp))
                Text(text = parts.getOrNull(1) ?: "", fontWeight = FontWeight.Bold)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            viewModel.question.options?.forEach { option ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectAnswer(option) }
                        .padding(horizontal = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.checkAnswer()
                isAnswerCorrect?.let { onResultValidated(it) }
            },
            enabled = selectedAnswer != null,
            modifier = Modifier
                .padding(top = 8.dp)
                .height(48.dp)
                .width(140.dp)
        ) {
            Text(text = "Submit")
        }

//        if (isAnswerCorrect != null) {
//            Text(
//                text = if (isAnswerCorrect) "Benar!" else "Jawaban salah",
//                color = if (isAnswerCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
    }
}
