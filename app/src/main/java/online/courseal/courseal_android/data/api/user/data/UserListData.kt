package online.courseal.courseal_android.data.api.user.data

import kotlinx.serialization.Serializable

@Serializable
data class UserListData(
    val usertag: String,
    val username: String,
    val xp: Int
)