package online.courseal.courseal_android.data.database

import androidx.room.TypeConverter
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun convertCookieToString(cookie: Cookie): String = Json.encodeToString(SerializableCookie(cookie))

    @TypeConverter
    fun convertStringToCookie(string: String): Cookie = Json.decodeFromString<SerializableCookie>(string).toCookie()

    @TypeConverter
    fun convertUrlToString(url: Url): String = Json.encodeToString(url)

    @TypeConverter
    fun convertStringToUrl(string: String): Url = Json.decodeFromString(string)
}

@Serializable
data class SerializableCookie(
    val name: String,
    val value: String,
    val encoding: CookieEncoding,
    val maxAge: Int,
    val expires: Long?,
    val domain: String?,
    val path: String?,
    val secure: Boolean,
    val httpOnly: Boolean
) {
    constructor(cookie: Cookie): this (
        name = cookie.name,
        value = cookie.value,
        encoding = cookie.encoding,
        maxAge = cookie.maxAge,
        expires = cookie.expires?.timestamp,
        domain = cookie.domain,
        path = cookie.path,
        secure = cookie.secure,
        httpOnly = cookie.httpOnly
    )

    fun toCookie(): Cookie {
        return Cookie(
            name = this.name,
            value = this.value,
            encoding = this.encoding,
            maxAge = this.maxAge,
            expires = GMTDate(timestamp = this.expires),
            domain = this.domain,
            path = this.path,
            secure = this.secure,
            httpOnly = this.httpOnly
        )
    }
}