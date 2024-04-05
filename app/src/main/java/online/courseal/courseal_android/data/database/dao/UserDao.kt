package online.courseal.courseal_android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ktor.http.Url
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.data.database.entities.UserCookie

@Dao
interface UserDao {
    /* User specific queries */
    @Insert
    suspend fun insertUser(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE usertag = :usertag AND server_id = :serverId")
    suspend fun findByUsertagServer(usertag: String, serverId: Long): User?

    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun findById(userId: Long): User?


    /* Current user queries */
    @Query("SELECT user.* FROM user " +
            "JOIN currentuser ON user.user_id = currentuser.user_id " +
            "WHERE currentuser.single = 1")
    suspend fun getCurrentUser(): User?

    @Query("REPLACE INTO currentuser(user_id, single) VALUES (:userId, 1);")
    suspend fun setCurrentUser(userId: Long?)


    /* Cookies queries */
    @Query("SELECT usercookie.* FROM usercookie " +
            "JOIN user ON usercookie.user_id = user.user_id " +
            "WHERE usercookie.user_id = :userId AND usercookie.request_url = :requestUrl")
    suspend fun getCookies(userId: Long, requestUrl: Url): List<UserCookie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCookie(userCookie: UserCookie): Long

    @Delete
    suspend fun deleteCookies(cookies: List<UserCookie>)
}