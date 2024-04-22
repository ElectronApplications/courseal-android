package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.ktor.http.Cookie

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cookie_name"], unique = true)
    ]
)
data class UserCookie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("user_cookie_id") val userCookieId: Long = 0,
    @ColumnInfo("request_url") val requestUrl: String,
    @ColumnInfo("cookie_name") val cookieName: String,
    @ColumnInfo("cookie") val cookie: Cookie,
    @ColumnInfo("user_id") val userId: Long
)