package com.example.skripsi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.viewmodel.MatchViewModel

@Composable
fun MatchScreen(viewModel: MatchViewModel = viewModel()) {
    val wordList = viewModel.wordList
    val selectedWords = viewModel.selectedWords
    val isAnswerCorrect = viewModel.isAnswerCorrect

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Susun kata menjadi suatu kalimat yang runut.", style = MaterialTheme.typography.titleMedium)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedWords) { word ->
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable { viewModel.removeWord(word) }
                        .padding(8.dp)
                )
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(wordList.filterNot { selectedWords.contains(it) }) { word ->
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable { viewModel.selectWord(word) }
                        .padding(8.dp)
                )
            }
        }

        if (isAnswerCorrect != null) {
            Text(
                text = if (isAnswerCorrect) "Benar!" else "Jawaban anda salah.",
                color = if (isAnswerCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
