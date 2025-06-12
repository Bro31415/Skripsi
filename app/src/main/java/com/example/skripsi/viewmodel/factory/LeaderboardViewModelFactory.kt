import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.LeaderboardViewModel

class LeaderboardViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}