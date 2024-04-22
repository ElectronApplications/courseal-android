package online.courseal.courseal_android.data.api.courseenrollment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseEnrollApiRequest(
    @SerialName("course_id") val courseId: Int
)

enum class CourseEnrollApiError {
    COURSE_NOT_FOUND,
    UNKNOWN
}