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
    @JsonNames("server_register_enabled") val serverRegisterEnabled: Boolean
)
suspend fun coursealInfo(url: String, onError: (errorCode: Int, errorMessage: String) -> Unit, onSuccess: (serverInfo: ServerInfo) -> Unit) {
    print("$url/api/courseal-info")
    try {
        val response = httpClient.get("$url/api/courseal-info")
        if(response.status.value == 200) {
            val result: ServerInfo = response.body()
            onSuccess(result)
        } else {
            onError(response.status.value, "")
        }
    } catch (ex: Exception) {
        onError(-1, ex.stackTraceToString())
    }
}