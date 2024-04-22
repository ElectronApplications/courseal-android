package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.api.user.data.MaintainerPermissions

@Serializable
data class CourseListData(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("maintainer_permissions") val maintainerPermissions: MaintainerPermissions
)