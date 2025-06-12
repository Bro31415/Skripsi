package com.example.skripsi.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.skripsi.viewmodel.MatchUiState

@Composable
fun MatchScreen(
    uiState: MatchUiState,
    onWordBankChipClicked: (String) -> Unit,
    onAnswerChipClicked: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onSubmit,
                enabled = uiState.selectedAnswerWords.isNotEmpty() && !uiState.isSubmitted,
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
                Text("Submit", style = MaterialTheme.typography.titleMedium)
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
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Susun kata menjadi kalimat yang benar",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = uiState.questionText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                AnswerDropArea(
                    selectedWords = uiState.selectedAnswerWords,
                    onAnswerChipClicked = onAnswerChipClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )
            }

            WordBank(
                options = uiState.wordBankOptions,
                onWordBankChipClicked = onWordBankChipClicked,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun AnswerDropArea(
    selectedWords: List<String>,
    onAnswerChipClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.defaultMinSize(minHeight = 120.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedWords.isEmpty()) {
                Text(
                    text = "...",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                selectedWords.chunked(3).forEach { rowOfWords ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowOfWords.forEach { word ->
                            WordChip(text = word, onClick = { onAnswerChipClicked(word) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordBank(
    options: List<String>,
    onWordBankChipClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.chunked(3).forEach { rowOfWords ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowOfWords.forEach { word ->
                    WordChip(text = word, onClick = { onWordBankChipClicked(word) })
                }
            }
        }
    }
}

@Composable
fun WordChip(text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}