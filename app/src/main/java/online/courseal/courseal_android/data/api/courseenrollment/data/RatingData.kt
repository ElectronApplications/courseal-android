package online.courseal.courseal_android.data.api.courseenrollment.data

import kotlinx.serialization.Serializable

@Serializable
data class RatingData(
    val rating: Int
)

enum class RatingApiError {
    COURSE_NOT_FOUND,
    UNKNOWN,
}