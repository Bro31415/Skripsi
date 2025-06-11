package com.example.skripsi.ui.course

import DictionaryDetailScreen
import DictionaryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skripsi.MyApp
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.ui.theme.AppTheme
import com.example.skripsi.viewmodel.DictionaryViewModel
import com.example.skripsi.viewmodel.factory.DictionaryViewModelFactory

class DictionaryActivity : ComponentActivity() {

    private val viewModel: DictionaryViewModel by viewModels {
        DictionaryViewModelFactory(
            CourseRepository(MyApp.supabase),
            DictionaryRepository(MyApp.supabase)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chapterId = intent.getLongExtra("chapterId", -1L)
        viewModel.loadDictionaryForChapter(chapterId)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val chapter by viewModel.chapter.collectAsState()
                val words by viewModel.words.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "list" ,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {

                    composable("list") {
                        DictionaryScreen(
                            chapter = chapter,
                            words = words,
                            onWordClick = { wordId ->
                                navController.navigate("detail/$wordId")
                            },
                            onBackClick = {
                                finish()
                            }
                        )
                    }

                    composable(
                        route = "detail/{wordId}",
                        arguments = listOf(navArgument("wordId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val wordId = backStackEntry.arguments?.getLong("wordId")
                        val word = wordId?.let { viewModel.getWordById(it) }
                        DictionaryDetailScreen(
                            word = word,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}