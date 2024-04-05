package online.courseal.courseal_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["single"], unique = true)])
data class CurrentUser(
    @PrimaryKey @ColumnInfo("user_id") val userId: Long?,
    val single: Boolean = true
)