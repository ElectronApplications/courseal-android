package online.courseal.courseal_android.data.api.auth

sealed class AuthWrapperError<E> {
    class JWTInvalid<E> : AuthWrapperError<E>()
    class InnerError<E>(val innerError: E) : AuthWrapperError<E>()
}