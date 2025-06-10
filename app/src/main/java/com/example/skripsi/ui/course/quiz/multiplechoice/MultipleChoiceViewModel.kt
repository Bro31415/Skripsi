import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

@Immutable
data class MultipleChoiceUiState(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val selectedAnswer: String? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean? = null
)

class MultipleChoiceViewModel(
    val question: Question
) : ViewModel() {

    var uiState by mutableStateOf(
        MultipleChoiceUiState(
            questionText = question.questionText,
            options = question.options.orEmpty(),
            correctAnswer = question.answer
        )
    )
        private set

    fun onAnswerSelected(option: String) {
        if (!uiState.isSubmitted) {
            uiState = uiState.copy(selectedAnswer = option)
        }
    }

    fun onSubmit() {
        if (uiState.selectedAnswer == null) return

        val isAnswerCorrect = uiState.selectedAnswer == question.answer
        uiState = uiState.copy(
            isSubmitted = true,
            isCorrect = isAnswerCorrect
        )
    }
}