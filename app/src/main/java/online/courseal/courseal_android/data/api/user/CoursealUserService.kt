package online.courseal.courseal_android.data.api.user

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

class CoursealUserService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao
) {
    private suspend fun userUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user"

    suspend fun usersList(search: String): ApiResult<List<UserListData>, Unit> {
        return try {
            val response = httpClient.get(userUrl()) {
                url {
                    parameters.append("search", search)
                }
            }

            if (response.status.value in 200..299)
                ApiResult.Success(response.body())
            else
                ApiResult.Error(Unit)
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(Unit)
            }
        }
    }

    suspend fun userInfo(usertag: String): ApiResult<UserApiResponse, UserApiError> {
        return try {
            val response = httpClient.get(userUrl()) {
                url {
                    appendPathSegments(usertag)
                }
            }

            if (response.status.value in 200..299)
                ApiResult.Success(response.body())
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.USER_NOT_FOUND -> ApiResult.Error(UserApiError.USER_NOT_FOUND)
                else -> ApiResult.Error(UserApiError.UNKNOWN)
            }
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(UserApiError.UNKNOWN)
            }
        }
    }

    suspend fun userActivity(usertag: String): ApiResult<UserActivityApiResponse, UserActivityApiError> {
        return try {
            val response = httpClient.get(userUrl()) {
                url {
                    appendPathSegments(usertag, "activity")
                }
            }

            if (response.status.value in 200..299)
                ApiResult.Success(response.body())
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.USER_NOT_FOUND -> ApiResult.Error(UserActivityApiError.USER_NOT_FOUND)
                else -> ApiResult.Error(UserActivityApiError.UNKNOWN)
            }
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(UserActivityApiError.UNKNOWN)
            }
        }
    }

}