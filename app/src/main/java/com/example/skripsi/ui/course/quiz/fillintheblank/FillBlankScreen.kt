
package com.example.skripsi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.viewmodel.FillBlankViewModel

@Composable
fun FillBlankScreen(
    viewModel: FillBlankViewModel = viewModel(),
    onBackPressed: () -> Unit = {}
) {
    val currentQuestionIndex = viewModel.currentQuestionIndex
    val totalQuestions = viewModel.totalQuestions
    val selectedOption = viewModel.selectedOption
    val currentQuestion = viewModel.currentQuestion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with back button and progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "${currentQuestionIndex + 1} dari $totalQuestions",
                fontSize = 14.sp
            )
        }

        // Instruction text
        Text(
            text = "Lengkapi kalimat di bawah!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 68.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        // Question and translation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentQuestion.questionText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentQuestion.translation,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 16.sp
            )
        }

        // Options grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 196.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OptionButton(
                    text = currentQuestion.options[0],
                    isSelected = selectedOption == 0,
                    onClick = { viewModel.selectOption(0) },
                    modifier = Modifier.weight(1f)
                )

                OptionButton(
                    text = currentQuestion.options[1],
                    isSelected = selectedOption == 1,
                    onClick = { viewModel.selectOption(1) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OptionButton(
                    text = currentQuestion.options[2],
                    isSelected = selectedOption == 2,
                    onClick = { viewModel.selectOption(2) },
                    modifier = Modifier.weight(1f)
                )

                OptionButton(
                    text = currentQuestion.options[3],
                    isSelected = selectedOption == 3,
                    onClick = { viewModel.selectOption(3) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White

    OutlinedCard(
        modifier = modifier
            .height(56.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.outlinedCardColors(
            containerColor = backgroundColor
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}