package online.courseal.courseal_android.data.api.courseenrollment

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.datetime.TimeZone
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.auth.data.AuthWrapperError
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
import online.courseal.courseal_android.data.api.courseenrollment.data.CompleteTasksApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.CompleteTasksApiRequest
import online.courseal.courseal_android.data.api.courseenrollment.data.CompleteTasksApiResponse
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollApiRequest
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollInfoApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollInfoApiResponse
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseListData
import online.courseal.courseal_android.data.api.courseenrollment.data.RatingApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.RatingData
import online.courseal.courseal_android.data.api.courseenrollment.data.TasksApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.TasksApiResponse
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksComplete
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

class CoursealCourseEnrollmentService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao,
    private val authService: CoursealAuthService
) {
    private suspend fun courseEnrollmentUrl(): String = "${serverDao.getCurrentServerUrl()}/api/course-enrollment"

    suspend fun coursesList(): ApiResult<List<CourseListData>, Unit> = authService.authWrap(Unit) {
        val response = httpClient.get(courseEnrollmentUrl())

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            else -> ApiResult.Error(AuthWrapperError.InnerError(Unit))
        }
    }

    suspend fun courseEnroll(courseId: Int): ApiResult<Unit, CourseEnrollApiError> = authService.authWrap(CourseEnrollApiError.UNKNOWN) {
        val response = httpClient.post(courseEnrollmentUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                CourseEnrollApiRequest(
                    courseId = courseId
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CourseEnrollApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CourseEnrollApiError.UNKNOWN))
        }
    }

    suspend fun courseInfo(courseId: Int): ApiResult<CourseEnrollInfoApiResponse, CourseEnrollInfoApiError> = authService.authWrap(CourseEnrollInfoApiError.UNKNOWN) {
        val response = httpClient.get(courseEnrollmentUrl()) {
            url {
                appendPathSegments("$courseId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CourseEnrollInfoApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CourseEnrollInfoApiError.UNKNOWN))
        }
    }

    suspend fun getRating(courseId: Int): ApiResult<RatingData, RatingApiError> = authService.authWrap(RatingApiError.UNKNOWN) {
        val response = httpClient.get(courseEnrollmentUrl()) {
            url {
                appendPathSegments("$courseId", "rating")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(RatingApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(RatingApiError.UNKNOWN))
        }
    }

    suspend fun setRating(courseId: Int, rating: Int): ApiResult<Unit, RatingApiError> = authService.authWrap(RatingApiError.UNKNOWN) {
        val response = httpClient.put(courseEnrollmentUrl()) {
            url {
                appendPathSegments("$courseId", "rating")
            }
            contentType(ContentType.Application.Json)
            setBody(RatingData(rating = rating))
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(RatingApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(RatingApiError.UNKNOWN))
        }
    }

    suspend fun getTasks(courseId: Int, lessonId: Int): ApiResult<TasksApiResponse, TasksApiError> = authService.authWrap(TasksApiError.UNKNOWN) {
        val response = httpClient.get(courseEnrollmentUrl()) {
            url {
                appendPathSegments("$courseId", "lesson", "$lessonId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(TasksApiError.COURSE_NOT_FOUND))
            ErrorResponseType.LESSON_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(TasksApiError.LESSON_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(TasksApiError.UNKNOWN))
        }
    }

    suspend fun completeTasks(courseId: Int, lessonId: Int, lessonToken: String, tasks: EnrollTasksComplete):
            ApiResult<CompleteTasksApiResponse, CompleteTasksApiError> = authService.authWrap(CompleteTasksApiError.UNKNOWN) {
        val response = httpClient.put(courseEnrollmentUrl()) {
            url {
                appendPathSegments("$courseId", "lesson", "$lessonId")
            }
            contentType(ContentType.Application.Json)
            setBody(
                CompleteTasksApiRequest(
                    lessonToken = lessonToken,
                    timezone = TimeZone.currentSystemDefault(),
                    results = tasks,
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CompleteTasksApiError.COURSE_NOT_FOUND))
            ErrorResponseType.LESSON_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CompleteTasksApiError.LESSON_NOT_FOUND))
            ErrorResponseType.LESSON_TOKEN_INVALID -> ApiResult.Error(AuthWrapperError.InnerError(CompleteTasksApiError.TOKEN_INVALID))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CompleteTasksApiError.UNKNOWN))
        }
    }
}