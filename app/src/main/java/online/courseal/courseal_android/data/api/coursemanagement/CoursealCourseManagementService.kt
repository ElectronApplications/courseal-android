package online.courseal.courseal_android.data.api.coursemanagement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.auth.AuthWrapperError
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

class CoursealCourseManagementService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao,
    private val authService: CoursealAuthService
) {
    private suspend fun courseManagementUrl(): String = "${serverDao.getCurrentServerUrl()}/api/course-management"

    suspend fun createCourse(courseName: String, courseDescription: String): ApiResult<CreateCourseApiResponse, CreateCourseApiError> = authService.authWrap(CreateCourseApiError.UNKNOWN) {
        val response = httpClient.post(courseManagementUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                CreateCourseApiRequest(
                    courseName = courseName,
                    courseDescription = courseDescription
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.CANT_CREATE_COURSES -> ApiResult.Error(AuthWrapperError.InnerError(CreateCourseApiError.CANT_CREATE_COURSES))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CreateCourseApiError.UNKNOWN))
        }
    }

    suspend fun coursesList(): ApiResult<List<CourseListData>, Unit> = authService.authWrap(Unit) {
        val response = httpClient.get(courseManagementUrl())

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            else -> ApiResult.Error(AuthWrapperError.InnerError(Unit))
        }
    }

    suspend fun courseInfo(courseId: Int): ApiResult<CourseApiResponse, CourseApiError> = authService.authWrap(CourseApiError.UNKNOWN) {
        val response = httpClient.get(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CourseApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CourseApiError.UNKNOWN))
        }
    }


}