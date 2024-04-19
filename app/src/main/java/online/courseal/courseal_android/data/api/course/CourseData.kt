package online.courseal.courseal_android.data.api.course

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.api.user.MaintainerPermissions

@Serializable
data class CourseApiResponse(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String,
    val votes: Int,
    @SerialName("course_maintainers") val courseMaintainers: List<CourseMaintainer>
)

@Serializable
data class CourseMaintainer(
    val usertag: String,
    @SerialName("maintainer_permissions") val maintainerPermissions: MaintainerPermissions
)

enum class CourseApiError {
    COURSE_NOT_FOUND,
    UNKNOWN
}