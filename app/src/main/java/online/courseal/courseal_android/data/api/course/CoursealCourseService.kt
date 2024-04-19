package online.courseal.courseal_android.data.api.course

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.database.dao.ServerDao
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class CoursealCourseService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao
) {
    private suspend fun courseUrl(): String = "${serverDao.getCurrentServerUrl()}/api/course"

    suspend fun coursesList(search: String): ApiResult<List<CourseListData>, Unit> {
        return try {
            val response = httpClient.get(courseUrl()) {
                url {
                    parameters.append("search", search)
                }
            }

            if (response.status.value in 200..299)
                ApiResult.Success(response.body())
            else
                ApiResult.Error(Unit)
        } catch (e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(Unit)
            }
        }
    }

    suspend fun courseInfo(courseId: Int): ApiResult<CourseApiResponse, CourseApiError> {
        return try {
            val response = httpClient.get(courseUrl()) {
                url {
                    appendPathSegments("$courseId")
                }
            }

            if (response.status.value in 200..299)
                ApiResult.Success(response.body())
            else when (response.body<ErrorResponse>().error) {
                ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(CourseApiError.COURSE_NOT_FOUND)
                else -> ApiResult.Error(CourseApiError.UNKNOWN)
            }
        } catch (e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(CourseApiError.UNKNOWN)
            }
        }
    }
}