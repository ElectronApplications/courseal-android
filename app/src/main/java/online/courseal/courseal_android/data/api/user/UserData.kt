package online.courseal.courseal_android.data.api.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiResponse(
    val usertag: String,
    val username: String,
    @SerialName("date_created") val dateCreated: String,
    val xp: Int,
    val courses: List<UserCourseInformation>,
    @SerialName("courses_maintainer") val coursesMaintainer: List<UserCourseMaintainer>
)

@Serializable
data class UserCourseInformation(
    @SerialName("course_id") val courseId: Int,
    val xp: Int
)

@Serializable
data class UserCourseMaintainer(
    @SerialName("course_id") val courseId: Int,
    @SerialName("maintainer_permissions") val maintainerPermissions: MaintainerPermissions
)

@Serializable
enum class MaintainerPermissions {
    @SerialName("full") FULL,
    @SerialName("edit_course") EDIT_COURSE,
    @SerialName("edit_lessons") EDIT_LESSONS
}

enum class UserApiError {
    USER_NOT_FOUND,
    UNKNOWN
}