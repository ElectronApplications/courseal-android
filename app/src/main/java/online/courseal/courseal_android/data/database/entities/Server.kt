package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["server_url"], unique = true)
    ],
)
data class Server(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("server_id") val serverId: Long = 0,
    @ColumnInfo("server_url") val serverUrl: String,
    @ColumnInfo("server_name") val serverName: String,
    @ColumnInfo("server_description") val serverDescription: String,
    @ColumnInfo("server_registration_enabled") val serverRegistrationEnabled: Boolean
)