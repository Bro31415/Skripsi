import kotlinx.serialization.Serializable

@Serializable
data class UserAchievement(
    val id: Long? = null,
    val user_id: String,         
    val achievement_key: String,
    val unlocked_at: String
)
