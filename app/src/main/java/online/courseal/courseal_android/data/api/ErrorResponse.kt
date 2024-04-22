package online.courseal.courseal_android.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: ErrorResponseType,
    val message: String?,
    val description: String?
)

@Serializable
enum class ErrorResponseType {
    @SerialName("jwt-invalid") JWT_INVALID,
    @SerialName("refresh-invalid") REFRESH_INVALID,
    @SerialName("refresh-not-found") REFRESH_NOT_FOUND,
    @SerialName("account-already-exist") USER_EXISTS,
    @SerialName("bad-request") BAD_REQUEST,
    @SerialName("registration-is-disabled") REGISTRATION_DISABLED,
    @SerialName("usertag-incorrect") INCORRECT_USERTAG,
    @SerialName("usertag-or-password-incorrect") INCORRECT_LOGIN,
    @SerialName("user-not-found") USER_NOT_FOUND,
    @SerialName("incorrect-password") INCORRECT_PASSWORD,
    @SerialName("course-not-found") COURSE_NOT_FOUND,
    @SerialName("cant-create-courses") CANT_CREATE_COURSES,
    @SerialName("no-permissions") NO_PERMISSIONS,
    @SerialName("lesson-not-found") LESSON_NOT_FOUND,
    @SerialName("lesson-token-invalid") LESSON_TOKEN_INVALID,
}