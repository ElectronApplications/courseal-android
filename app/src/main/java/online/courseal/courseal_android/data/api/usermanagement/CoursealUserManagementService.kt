package online.courseal.courseal_android.data.api.usermanagement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.auth.data.AuthWrapperError
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
import online.courseal.courseal_android.data.api.httpExceptionWrap
import online.courseal.courseal_android.data.api.usermanagement.data.ChangeNameApiError
import online.courseal.courseal_android.data.api.usermanagement.data.ChangeNameApiRequest
import online.courseal.courseal_android.data.api.usermanagement.data.ChangePasswordApiError
import online.courseal.courseal_android.data.api.usermanagement.data.ChangePasswordApiRequest
import online.courseal.courseal_android.data.api.usermanagement.data.RegistrationApiError
import online.courseal.courseal_android.data.api.usermanagement.data.RegistrationApiRequest
import online.courseal.courseal_android.data.api.usermanagement.data.UserManagementApiError
import online.courseal.courseal_android.data.api.usermanagement.data.UserManagementApiResponse
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

class CoursealUserManagementService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao,
    private val authService: CoursealAuthService
) {
    private suspend fun userManagementUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user-management"
    private suspend fun registrationUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user-management/register"
    private suspend fun usernameUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user-management/username"
    private suspend fun passwordUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user-management/password"

    suspend fun userInfo(): ApiResult<UserManagementApiResponse, UserManagementApiError> = authService.authWrap(UserManagementApiError.UNKNOWN) {
        val response = httpClient.get(userManagementUrl())

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            else -> ApiResult.Error(AuthWrapperError.InnerError(UserManagementApiError.UNKNOWN))
        }
    }

    suspend fun register(usertag: String, username: String, password: String): ApiResult<Unit, RegistrationApiError> = httpExceptionWrap(RegistrationApiError.UNKNOWN) {
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

        return@httpExceptionWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.USER_EXISTS -> ApiResult.Error(RegistrationApiError.USER_EXISTS)
            ErrorResponseType.INCORRECT_USERTAG -> ApiResult.Error(RegistrationApiError.INCORRECT_USERTAG)
            else -> ApiResult.Error(RegistrationApiError.UNKNOWN)
        }
    }

    suspend fun changeUsername(newUsername: String): ApiResult<Unit, ChangeNameApiError> = authService.authWrap(ChangeNameApiError.UNKNOWN) {
        val response = httpClient.put(usernameUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                ChangeNameApiRequest(username = newUsername)
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            else -> ApiResult.Error(AuthWrapperError.InnerError(ChangeNameApiError.BAD_REQUEST))
        }
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): ApiResult<Unit, ChangePasswordApiError> = authService.authWrap(ChangePasswordApiError.UNKNOWN) {
        val response = httpClient.put(passwordUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                ChangePasswordApiRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.INCORRECT_PASSWORD -> ApiResult.Error(AuthWrapperError.InnerError(ChangePasswordApiError.PASSWORD_INVALID))
            else -> ApiResult.Error(AuthWrapperError.InnerError(ChangePasswordApiError.BAD_REQUEST))
        }
    }
}