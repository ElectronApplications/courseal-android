package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["usertag", "server_id"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("user_id") val userId: Long = 0,
    @ColumnInfo("usertag") val usertag: String,
    @ColumnInfo("server_id") val serverId: Long
)