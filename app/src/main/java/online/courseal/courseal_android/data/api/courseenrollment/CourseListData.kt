package online.courseal.courseal_android.data.api.courseenrollment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseListData(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String,
    val xp: Int
)