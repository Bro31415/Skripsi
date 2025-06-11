import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skripsi.data.model.Chapter
import com.example.skripsi.data.model.Dictionary
import com.example.skripsi.viewmodel.ChapterWithWords

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    chapter: Chapter?,
    words: List<Dictionary>,
    onWordClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE6F0FF))) {
        TopAppBar(
            title = {
                Text("Kamus", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Bagian ${chapter?.id ?: ""}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Text(
                text = chapter?.name ?: "Memuat...",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(words) { word ->
                WordItem(word = word, onClick = { onWordClick(word.id) })
            }
        }
    }
}

@Composable
fun WordItem(word: Dictionary, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = word.word,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryDetailScreen(
    word: Dictionary?,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFEBF2FF))) {
        TopAppBar(
            title = {
                Text("Kamus",
                style = MaterialTheme.typography.headlineSmall
                    ) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        if (word == null) {
            Text("Kata tidak ditemukan.", modifier = Modifier.padding(16.dp))
        } else {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = word.word, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "Definisi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = word.definition, style = MaterialTheme.typography.bodyMedium, lineHeight = 24.sp)

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Penggunaan dalam kalimat", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = word.example, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
                Text(text = word.translation, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}