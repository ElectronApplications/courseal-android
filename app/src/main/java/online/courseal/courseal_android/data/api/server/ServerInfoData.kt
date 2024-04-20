package online.courseal.courseal_android.data.api.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(
    @SerialName("server_name") val serverName: String,
    @SerialName("server_description") val serverDescription: String,
    @SerialName("server_registration_enabled") val serverRegistrationEnabled: Boolean
)