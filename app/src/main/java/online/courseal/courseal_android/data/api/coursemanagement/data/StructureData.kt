package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StructureData(
    @SerialName("lesson_id") val lessonId: Int
)

enum class StructureApiError {
    NO_PERMISSIONS,
    UNKNOWN
}