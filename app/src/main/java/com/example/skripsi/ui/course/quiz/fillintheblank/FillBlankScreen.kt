package com.example.skripsi.ui.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.skripsi.viewmodel.FillInTheBlankUiState

@Composable
fun FillInTheBlankScreen(
    uiState: FillInTheBlankUiState,
    onAnswerSelected: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onSubmit,
                colors = ButtonColors(Color(0xFF6AFF8A), Color.Black, Color(0xFF6AFF8A), Color.Black),
                enabled = uiState.selectedAnswer != null && !uiState.isSubmitted,
                shape = RoundedCornerShape(15.dp),
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Lengkapi kalimat di bawah!",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = uiState.mainQuestion,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 48.dp)
                )

                SentenceDisplay(
                    parts = uiState.sentenceParts,
                    selectedAnswer = uiState.selectedAnswer
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(uiState.options) { option ->
                    WordBankOption(
                        text = option,
                        isSelected = uiState.selectedAnswer == option,
                        isSubmitted = uiState.isSubmitted,
                        isCorrect = uiState.isCorrect,
                        onClick = { onAnswerSelected(option) }
                    )
                }
            }
        }
    }
}


@Composable
fun SentenceDisplay(parts: List<String>, selectedAnswer: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        Text(
            text = parts.getOrNull(0) ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = parts.getOrNull(1) ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WordBankOption(
    text: String,
    isSelected: Boolean,
    isSubmitted: Boolean,
    isCorrect: Boolean?,
    onClick: () -> Unit
) {
    val greenColor = Color(0xFF6AFF8A)
    val redColor = Color(0xFFD32F2F)
    val defaultBorderColor = Color.LightGray
    val selectedBorderColor = Color(0xFF40A255)

    val borderColor by animateColorAsState(
        targetValue = when {
            isSubmitted && isSelected && isCorrect == true -> selectedBorderColor
            isSubmitted && isSelected && isCorrect == false -> redColor
            isSelected -> selectedBorderColor
            else -> defaultBorderColor
        }, label = "Border Color Animation"
    )

    val cardColor by animateColorAsState(
        targetValue = when {
            isSubmitted && isSelected && isCorrect == true -> greenColor
            isSubmitted && isSelected && isCorrect == false -> redColor
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