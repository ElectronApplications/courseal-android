package online.courseal.courseal_android.data.api

import android.util.Log
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.getTimeMillis
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.UserCookie
import javax.inject.Inject

class UserCookiesStorage @Inject constructor(
    private val userDao: UserDao
) : CookiesStorage {

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val currentUser = userDao.getCurrentUser()
        if (currentUser != null) {
            userDao.insertCookie(UserCookie(
                requestUrl = requestUrl.host,
                cookieName = cookie.name,
                cookie = cookie,
                userId = currentUser.userId
            ))
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val currentUser = userDao.getCurrentUser()
        return if (currentUser != null) {
            val userCookies = userDao.getCookies(currentUser.userId, requestUrl.host)
            val expiredCookies = userCookies.filter { (it.cookie.expires?.timestamp ?: Long.MAX_VALUE) <= getTimeMillis() }
            userDao.deleteCookies(expiredCookies)
            userCookies.map { it.cookie }.filterNot { (it.expires?.timestamp ?: Long.MAX_VALUE) <= getTimeMillis() }
        } else {
            emptyList()
        }
    }

    override fun close() {
        // For now there is nothing to do here
    }

}