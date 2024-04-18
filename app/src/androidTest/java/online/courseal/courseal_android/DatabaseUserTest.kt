package online.courseal.courseal_android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.ktor.http.Cookie
import kotlinx.coroutines.test.runTest
import online.courseal.courseal_android.data.database.CoursealDatabase
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.data.database.entities.UserCookie
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseUserTest {
    private lateinit var serverDao: ServerDao
    private lateinit var userDao: UserDao
    private lateinit var db: CoursealDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoursealDatabase::class.java).build()
        serverDao = db.serverDao()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun addAndGetCookies() = runTest {
        val server = Server(
            serverUrl = "https://example.com",
            serverName = "",
            serverDescription = "",
            serverRegistrationEnabled = false
        )
        val serverId = serverDao.upsertServer(server)

        val user = User(
            usertag = "example",
            loggedIn = true,
            serverId = serverId
        )
        val userId = userDao.insertUser(user)
        userDao.setCurrentUser(userId)

        val cookie = Cookie(
            name = "courseal_jwt",
            value = "tes.test.est",
            maxAge = 1000000
        )

        val userCookie = UserCookie(
            requestUrl = "https://example.com",
            cookie = cookie,
            userId = userId
        )
        userDao.insertCookie(userCookie)

        val cookiesList = userDao.getCookies(userId, "https://example.com")
        assert(cookiesList.any { it.cookie.name == cookie.name && it.cookie.value == cookie.value })
    }

    @Test
    fun deleteCurrentUser() = runTest {
        val server = Server(
            serverUrl = "https://example.com",
            serverName = "",
            serverDescription = "",
            serverRegistrationEnabled = false
        )
        val serverId = serverDao.upsertServer(server)

        val user = User(
            usertag = "example",
            loggedIn = true,
            serverId = serverId
        )
        val userId = userDao.insertUser(user)
        userDao.setCurrentUser(userId)

        userDao.deleteUserById(userId)

        val currentUser = userDao.getCurrentUser()

        assertEquals(null, currentUser)
    }
}