package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCourseApiRequest(
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String
)

enum class UpdateCourseApiError {
    NO_PERMISSIONS,
    UNKNOWN
}