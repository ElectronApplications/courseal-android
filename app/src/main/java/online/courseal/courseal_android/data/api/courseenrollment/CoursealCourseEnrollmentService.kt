package online.courseal.courseal_android.data.api.courseenrollment

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.auth.AuthWrapperError
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
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
}