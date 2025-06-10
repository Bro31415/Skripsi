package com.example.skripsi.ui.course

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.skripsi.viewmodel.DictionaryViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel, onSpeak: (String) -> Unit) {
    val entries = viewModel.entries.collectAsState().value
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "📖 Kamus",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (entries.isEmpty()) {
            Text("Tidak ada entri kamus untuk bab ini.")
        } else {
            val entry = entries[currentIndex]

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = entry,
                    modifier = Modifier.padding(16.dp)
                ) { animatedEntry ->
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = animatedEntry.word,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onSpeak(animatedEntry.word) }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Dengarkan kata")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("📌 Definisi:\n${animatedEntry.definition}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = animatedEntry.example,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onSpeak(animatedEntry.example) }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Dengarkan contoh")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🔄 Terjemahan:\n${animatedEntry.translation}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Previous")
                }

                Text(
                    text = "${currentIndex + 1} / ${entries.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = { if (currentIndex < entries.size - 1) currentIndex++ },
                    enabled = currentIndex < entries.size - 1
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}
