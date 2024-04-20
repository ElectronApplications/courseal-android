package online.courseal.courseal_android.data.api.coursemanagement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.api.user.MaintainerPermissions

@Serializable
data class CourseListData(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("maintainer_permissions") val maintainerPermissions: MaintainerPermissions
)