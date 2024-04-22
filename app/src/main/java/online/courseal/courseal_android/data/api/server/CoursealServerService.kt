package online.courseal.courseal_android.data.api.server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import online.courseal.courseal_android.data.api.server.data.ServerInfo
import javax.inject.Inject

class CoursealServerService @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun coursealInfo(url: String): ServerInfo? {
        return try {
            val response = httpClient.get("$url/api/courseal-info")

            if(response.status.value in 200..299) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
