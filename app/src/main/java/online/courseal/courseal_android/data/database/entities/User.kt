package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["usertag", "server_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Server::class,
            parentColumns = ["server_id"],
            childColumns = ["server_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("user_id") val userId: Long = 0,
    @ColumnInfo("usertag") val usertag: String,
    @ColumnInfo("logged_in") val loggedIn: Boolean,
    @ColumnInfo("server_id") val serverId: Long,
    @ColumnInfo("current_course_id") val currentCourseId: Int? = null,
    @ColumnInfo("current_editor_course_id") val currentEditorCourseId: Int? = null
)