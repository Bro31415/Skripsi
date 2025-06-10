package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.Chapter
import com.example.skripsi.data.model.ChapterWithQuizzes
import com.example.skripsi.data.model.Quiz
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.ui.theme.AppTheme
import com.example.skripsi.ui.theme.AppTypography
import com.example.skripsi.viewmodel.CourseViewModel
import com.example.skripsi.viewmodel.factory.CourseViewModelFactory


data class Lesson(
    val id: Long,
    val type: String,
    val title: String,
    val iconResId: Int,
)

class CourseFragment : Fragment() {

    private val viewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(CourseRepository(MyApp.supabase), MyApp.supabase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val chaptersWithQuizzes by viewModel.chapterWithQuizzes.collectAsState()
                val listItems = flattenAndMapToListItems(chaptersWithQuizzes)
                val username by viewModel.username.collectAsState()
                    AppTheme {
                        CourseScreen(
                            listItems = listItems,
                            userName = username,
                            onLessonClicked = { lesson ->
                                when(lesson.type) {
                                    "Quiz" -> {
                                        val intent = Intent(requireContext(), QuizRunnerActivity::class.java)
                                        intent.putExtra("quizId", lesson.id)
                                        startActivity(intent)
                                    }
                                    "Dictionary" -> {
                                        val intent = Intent(requireContext(), DictionaryActivity::class.java)
                                        intent.putExtra("chapterId", lesson.id)
                                        startActivity(intent)
                                    }
                                }
                            }
                        )
                    }

            }
        }
    }

    private fun flattenAndMapToListItems(chapters: List<ChapterWithQuizzes>): List<Lesson> {
        val lessonList = mutableListOf<Lesson>()

        chapters.forEach { chapterWithQuizzes ->
            lessonList.add(
                Lesson(
                    id = chapterWithQuizzes.chapter.id,
                    type = "Dictionary",
                    title = "${chapterWithQuizzes.chapter.name}",
                    iconResId = R.drawable.bold_book,
                )
            )

            chapterWithQuizzes.quizzes.forEach { quiz ->
                lessonList.add(
                    Lesson(
                        id = quiz.id,
                        type = "Quiz",
                        title = quiz.quizType,
                        iconResId = R.drawable.ic_edit,
                    )
                )
            }
        }
        return lessonList
    }
}


@Composable
fun CourseScreen(
    listItems: List<Lesson>,
    userName: String,
    onLessonClicked: (Lesson) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F0FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Halo, $userName!", style = MaterialTheme.typography.titleMedium)
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listItems) { lesson ->
                when (lesson.type) {
                    "Dictionary" -> ChapterHeaderItem(lesson = lesson, onClick = { onLessonClicked(lesson) })
                    "Quiz" -> LessonItem(lesson = lesson, onClick = { onLessonClicked(lesson) })
                }
            }
        }
    }
}

@Composable
fun ChapterHeaderItem(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D68F8))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Bagian ${lesson.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color(0xFF6AFF8A), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bold_book),
                    contentDescription = "Chapter Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun LessonItem(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = lesson.title,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}