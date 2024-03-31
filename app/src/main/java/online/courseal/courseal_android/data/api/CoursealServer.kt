package online.courseal.courseal_android.data.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ServerInfo(
    @JsonNames("server_name") val serverName: String,
    @JsonNames("server_description") val serverDescription: String,
    @JsonNames("server_registration_enabled") val serverRegistrationEnabled: Boolean
)
suspend fun coursealInfo(url: String): ServerInfo? {
    print("$url/api/courseal-info")
    return try {
        val response = httpClient.get("$url/api/courseal-info")
        if(response.status.value == 200) {
            response.body()
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}