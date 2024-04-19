package online.courseal.courseal_android.data.api.usermanagement

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.api.user.UserCourseInformation
import online.courseal.courseal_android.data.api.user.UserCourseMaintainer
enum class UserManagementApiError {
    UNKNOWN
}

@Serializable
data class UserManagementApiResponse(
    val usertag: String,
    val username: String,
    @SerialName("date_created") val dateCreated: LocalDateTime,
    @SerialName("can_create_courses") val canCreateCourses: Boolean,
    val xp: Int,
    @SerialName("profile_image_url") val profileImageUrl: String,
    val courses: List<UserCourseInformation>,
    @SerialName("courses_maintainer") val coursesMaintainer: List<UserCourseMaintainer>
)