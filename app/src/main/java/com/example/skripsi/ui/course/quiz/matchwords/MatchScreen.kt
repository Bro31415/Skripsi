package com.example.skripsi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.data.model.Question
import com.example.skripsi.viewmodel.MatchViewModel


@Composable
fun MatchScreen(
    viewModel: MatchViewModel = viewModel(),
    onResultValidated: (Boolean) -> Unit
    ) {
    MatchScreenContent(
        questionText = viewModel.questionText,
        wordList = viewModel.wordList ?: emptyList(),
        selectedWords = viewModel.selectedWords,
        isAnswerCorrect = viewModel.isAnswerCorrect,
        onSelectWord = viewModel::selectWord,
        onRemoveWord = viewModel::removeWord,
        onSubmit = {
            val isCorrect = viewModel.checkAnswer()
            onResultValidated(isCorrect)
        }
    )
}


@Composable
fun MatchScreenContent(
    questionText: String,
    wordList: List<String>,
    selectedWords: List<String>,
    isAnswerCorrect: Boolean?,
    onSelectWord: (String) -> Unit,
    onRemoveWord: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Susun kata menjadi suatu kalimat yang runut.", style = MaterialTheme.typography.titleMedium)

        Text(
            text = questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedWords) { word ->
                Text(
                    text = word,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable { onRemoveWord(word) }
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
                        .clickable { onSelectWord(word) }
                        .padding(8.dp)
                )
            }
        }

        Button(
            onClick = {
                onSubmit()
            },
            enabled = selectedWords.isNotEmpty()
        ) {
            Text("Submit")
        }
    }

}

@Preview
@Composable
fun MatchScreenPreview() {
    MatchScreenContent(
        questionText = "Question",
        wordList = listOf("test1", "test2", "test3"),
        selectedWords = listOf("test1", "test2"),
        isAnswerCorrect = false,
        onRemoveWord = {},
        onSelectWord = {},
        onSubmit = {}
    )
}