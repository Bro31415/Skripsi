package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.MyApp
import com.example.skripsi.data.model.ChapterWithQuizzes
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.viewmodel.CourseViewModel
import com.example.skripsi.viewmodel.factory.CourseViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CourseFragment : Fragment() {

    private val viewModel: CourseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val supabase = MyApp.supabase
        val repository = CourseRepository(supabase)
        val viewModelFactory = CourseViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[CourseViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                val chapters by viewModel.chapterWithQuizzes.collectAsState()

                MaterialTheme {
                    CourseScreen(
                        chapters = chapters,
                        onQuizClick = { quizId ->
                            val intent = Intent(requireContext(), QuizRunnerActivity::class.java)
                            intent.putExtra("quizId", quizId)
                            startActivity(intent)
                        }
                    )
                }
            }
        }


    }

    @Composable
    fun CourseScreen(
        chapters: List<ChapterWithQuizzes>,
        onQuizClick: (Long) -> Unit
    ) {
        if (chapters.isEmpty()) {
            Text("No chapters loaded", modifier = Modifier.padding(16.dp))
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            chapters.forEach { chapterWithQuizzes ->
                item {
                    Column {
                        Text(
                            text = chapterWithQuizzes.chapter.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        chapterWithQuizzes.quizzes.forEach { quiz ->
                            Button(
                                onClick = { onQuizClick(quiz.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(text = quiz.quizType)
                            }
                        }
                    }
                }
            }
        }
    }
}