package online.courseal.courseal_android.data.coursedata.lessons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("exam")
data class CoursealLessonExam(
    val tasks: List<Int>
) : CoursealLesson()