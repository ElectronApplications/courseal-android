package online.courseal.courseal_android.data.api.user

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.httpExceptionWrap
import online.courseal.courseal_android.data.api.user.data.UserActivityApiError
import online.courseal.courseal_android.data.api.user.data.UserActivityApiResponse
import online.courseal.courseal_android.data.api.user.data.UserApiError
import online.courseal.courseal_android.data.api.user.data.UserApiResponse
import online.courseal.courseal_android.data.api.user.data.UserListData
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

class CoursealUserService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao
) {
    private suspend fun userUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user"

    suspend fun usersList(search: String): ApiResult<List<UserListData>, Unit> = httpExceptionWrap(Unit) {
        val response = httpClient.get(userUrl()) {
            url {
                parameters.append("search", search)
            }
        }

        return@httpExceptionWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else
            ApiResult.Error(Unit)
    }

    suspend fun userInfo(usertag: String): ApiResult<UserApiResponse, UserApiError> = httpExceptionWrap(UserApiError.UNKNOWN) {
        val response = httpClient.get(userUrl()) {
            url {
                appendPathSegments(usertag)
            }
        }

        return@httpExceptionWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when(response.body<ErrorResponse>().error) {
            ErrorResponseType.USER_NOT_FOUND -> ApiResult.Error(UserApiError.USER_NOT_FOUND)
            else -> ApiResult.Error(UserApiError.UNKNOWN)
        }
    }

    suspend fun userActivity(usertag: String): ApiResult<UserActivityApiResponse, UserActivityApiError> = httpExceptionWrap(UserActivityApiError.UNKNOWN) {
        val response = httpClient.get(userUrl()) {
            url {
                appendPathSegments(usertag, "activity")
            }
        }

        return@httpExceptionWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when(response.body<ErrorResponse>().error) {
            ErrorResponseType.USER_NOT_FOUND -> ApiResult.Error(UserActivityApiError.USER_NOT_FOUND)
            else -> ApiResult.Error(UserActivityApiError.UNKNOWN)
        }
    }

}