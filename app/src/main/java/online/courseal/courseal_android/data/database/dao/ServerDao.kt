package online.courseal.courseal_android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import online.courseal.courseal_android.data.database.entities.Server

@Dao
interface ServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: Server): Long

    @Delete
    suspend fun deleteServer(server: Server)

    @Query("SELECT * FROM server WHERE server_id = :serverId")
    suspend fun findServerById(serverId: Long): Server

    @Query("SELECT server.* FROM server " +
            "JOIN user ON user.server_id = server.server_id " +
            "JOIN currentuser ON user.user_id = currentuser.user_id " +
            "WHERE currentuser.single = 1")
    suspend fun getCurrentServer(): Server?
}