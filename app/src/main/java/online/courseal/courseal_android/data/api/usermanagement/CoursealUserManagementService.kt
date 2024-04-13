package online.courseal.courseal_android.data.api.usermanagement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.database.dao.ServerDao
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class CoursealUserManagementService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao
) {
    private suspend fun registrationUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user-management/register"

    suspend fun register(usertag: String, username: String, password: String): ApiResult<Unit, RegistrationApiError> {
        return try {
            val response = httpClient.post(registrationUrl()) {
                contentType(ContentType.Application.Json)
                setBody(
                    RegistrationApiRequest(
                        usertag = usertag,
                        username = username,
                        password = password
                    )
                )
            }

            if (response.status.value == 200)
                ApiResult.Success(Unit)
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.USER_EXISTS -> ApiResult.Error(RegistrationApiError.USER_EXISTS)
                ErrorResponseType.INCORRECT_USERTAG -> ApiResult.Error(RegistrationApiError.INCORRECT_USERTAG)
                else -> ApiResult.Error(RegistrationApiError.UNKNOWN)
            }
        }
        catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(RegistrationApiError.UNKNOWN)
            }
        }
    }
}