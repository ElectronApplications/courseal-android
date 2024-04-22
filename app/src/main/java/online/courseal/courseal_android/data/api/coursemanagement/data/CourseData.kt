package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseApiResponse(
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String,
    val votes: Int,
    @SerialName("last_updated_structure") val lastUpdatedStructure: LocalDateTime,
    @SerialName("last_updated_lessons") val lastUpdatedLessons: LocalDateTime,
    @SerialName("last_updated_tasks") val lastUpdatedTasks: LocalDateTime
)

enum class CourseApiError {
    COURSE_NOT_FOUND,
    UNKNOWN
}