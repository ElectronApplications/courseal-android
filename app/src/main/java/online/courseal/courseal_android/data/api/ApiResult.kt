package online.courseal.courseal_android.data.api

enum class UnrecoverableErrorType {
    REFRESH_INVALID,
    SERVER_NOT_RESPONDING,
    OTHER_UNRECOVERABLE
}

sealed class ApiResult<T, E> {
    class Success<T, E>(val successValue: T) : ApiResult<T, E>()
    class Error<T, E>(val errorValue: E) : ApiResult<T, E>()
    class UnrecoverableError<T, E>(val unrecoverableType: UnrecoverableErrorType) : ApiResult<T, E>()
}

fun<T, E, U> ApiResult<T, E>.map(f: (T) -> U): ApiResult<U, E> {
    return when (this) {
        is ApiResult.UnrecoverableError -> ApiResult.UnrecoverableError(this.unrecoverableType)
        is ApiResult.Error -> ApiResult.Error(this.errorValue)
        is ApiResult.Success -> ApiResult.Success(f(this.successValue))
    }
}

fun<T, E, U> ApiResult<T, E>.mapErr(f: (E) -> U): ApiResult<T, U> {
    return when (this) {
        is ApiResult.UnrecoverableError -> ApiResult.UnrecoverableError(this.unrecoverableType)
        is ApiResult.Error -> ApiResult.Error(f(this.errorValue))
        is ApiResult.Success -> ApiResult.Success(this.successValue)
    }
}