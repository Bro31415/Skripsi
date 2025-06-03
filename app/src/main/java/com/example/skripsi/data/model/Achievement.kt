import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val id: String,
    val key: String,
    val name: String,
    val description: String,
    val created_at: String
)
