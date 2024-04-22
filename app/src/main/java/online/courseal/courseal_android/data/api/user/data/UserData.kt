package online.courseal.courseal_android.data.api.user.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserApiResponse(
    val usertag: String,
    val username: String,
    @SerialName("date_created") val dateCreated: LocalDateTime,
    @SerialName("can_create_courses") val canCreateCourses: Boolean,
    val xp: Int,
    @SerialName("profile_image_url") val profileImageUrl: String,
    val courses: List<UserCourseInformation>,
    @SerialName("courses_maintainer") val coursesMaintainer: List<UserCourseMaintainer>
)

@Serializable
data class UserCourseInformation(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    val xp: Int
)

@Serializable
data class UserCourseMaintainer(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("maintainer_permissions") val maintainerPermissions: MaintainerPermissions
)

@Serializable
enum class MaintainerPermissions {
    @SerialName("none") NONE,
    @SerialName("full") FULL,
    @SerialName("edit_course") EDIT_COURSE,
    @SerialName("edit_lessons") EDIT_LESSONS
}

enum class UserApiError {
    USER_NOT_FOUND,
    UNKNOWN
}