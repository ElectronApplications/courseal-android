package online.courseal.courseal_android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import online.courseal.courseal_android.data.database.CoursealDatabase
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseServerTest {
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
    fun insertServerAndUserThenUpdateServer() = runTest {
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

        val updatedServer = server.copy(
            serverId = serverId,
            serverUrl = "https://courseal.online"
        )
        serverDao.upsertServer(updatedServer)

        val userAfterUpdate = userDao.findUserByUsertagServer("example", serverId)

        assertEquals(userId, userAfterUpdate!!.userId)
    }

    @Test
    fun getServerByCurrentUser() = runTest {
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

        val currentServer = serverDao.getCurrentServer()

        assertEquals(serverId, currentServer!!.serverId)
    }

    @Test
    fun deleteCurrentServer() = runTest {
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

        val currentServer = serverDao.getCurrentServer()
        serverDao.deleteServer(currentServer!!)

        val currentUser = userDao.getCurrentUser()

        assertEquals(null, currentUser)
    }
}