package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.http.Cookie
import io.ktor.http.Url

@Entity
data class UserCookie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("user_cookie_id") val userCookieId: Long = 0,
    @ColumnInfo("request_url") val requestUrl: Url,
    @ColumnInfo("cookie") val cookie: Cookie,
    @ColumnInfo("user_id") val userId: Long
)