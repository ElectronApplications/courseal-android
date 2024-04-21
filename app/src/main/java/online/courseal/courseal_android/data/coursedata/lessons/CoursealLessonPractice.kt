package online.courseal.courseal_android.data.coursedata.lessons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("practice")
data class CoursealLessonPractice(
    val tasks: List<Int>
) : CoursealLesson()