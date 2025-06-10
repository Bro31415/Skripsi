package com.example.skripsi.ui.course.quiz.multiplechoice

import MultipleChoiceUiState
import MultipleChoiceViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.skripsi.data.model.Question
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.skripsi.ui.course.QuizRunnerActivity

@Composable
fun MultipleChoiceScreen(
    uiState: MultipleChoiceUiState,
    onAnswerSelected: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onSubmit,
                enabled = uiState.selectedAnswer != null && !uiState.isSubmitted,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6AFF8A),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        containerColor = Color(0xFFE6F0FF)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pilih arti yang sesuai!",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = uiState.questionText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(uiState.options) { option ->
                    AnswerOption(
                        text = option,
                        isSelected = uiState.selectedAnswer == option,
                        isSubmitted = uiState.isSubmitted,
                        isCorrectOption = option == uiState.correctAnswer,
                        onClick = { onAnswerSelected(option) }
                    )
                }
            }
        }
    }
}

@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    isSubmitted: Boolean,
    isCorrectOption: Boolean,
    onClick: () -> Unit
) {
    val greenColor = Color(0xFF6AFF8A)
    val redColor = Color(0xFFD32F2F)
    val defaultBorderColor = Color.LightGray
    val selectedBorderColor = Color(0xFF40A255)

    val borderColor by animateColorAsState(
        targetValue = when {
            isSubmitted && isCorrectOption -> selectedBorderColor
            isSubmitted && isSelected && !isCorrectOption -> redColor
            isSelected -> selectedBorderColor
            else -> defaultBorderColor
        }, label = "Border Color Animation"
    )

    val cardColor by animateColorAsState(
        targetValue = when {
            isSubmitted && isCorrectOption -> greenColor
            isSubmitted && isSelected && !isCorrectOption -> redColor
            isSelected -> greenColor
            else -> Color.White
        }, label = "Card Color Animation"
    )

    Card(
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

