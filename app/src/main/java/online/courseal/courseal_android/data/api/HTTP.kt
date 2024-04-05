package online.courseal.courseal_android.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
        install(HttpCookies) {
            storage = UserCookiesStorage()
        }
    }
}