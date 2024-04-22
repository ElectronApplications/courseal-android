package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCourseApiRequest(
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String
)

@Serializable
data class CreateCourseApiResponse(
    @SerialName("course_id") val courseId: Int,
)

enum class CreateCourseApiError {
    CANT_CREATE_COURSES,
    UNKNOWN
}