package online.courseal.courseal_android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import online.courseal.courseal_android.data.database.entities.Server

@Dao
interface ServerDao {

    @Upsert
    suspend fun upsertServer(server: Server): Long

    @Delete
    suspend fun deleteServer(server: Server)

    @Query("SELECT * FROM server WHERE server_id = :serverId")
    suspend fun findServerById(serverId: Long): Server?

    @Query("SELECT * FROM server WHERE server_url = :serverUrl")
    suspend fun findServerByUrl(serverUrl: String): Server?

    @Query("SELECT server.* FROM server " +
            "JOIN user ON user.server_id = server.server_id " +
            "JOIN currentuser ON user.user_id = currentuser.user_id " +
            "WHERE currentuser.single = 1")
    suspend fun getCurrentServer(): Server?

    @Query("SELECT server.server_url FROM server " +
            "JOIN user ON user.server_id = server.server_id " +
            "JOIN currentuser ON user.user_id = currentuser.user_id " +
            "WHERE currentuser.single = 1")
    suspend fun getCurrentServerUrl(): String?
}