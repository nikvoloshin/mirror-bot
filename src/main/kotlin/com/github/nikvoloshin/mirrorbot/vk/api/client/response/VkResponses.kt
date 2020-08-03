package com.github.nikvoloshin.mirrorbot.vk.api.client.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.nikvoloshin.mirrorbot.vk.api.client.adapter.CallbackSettingsDeserializer

data class CallbackConfirmationCode(val code: String)

data class ServerId(@JsonProperty("server_id") val serverId: Int)

data class CallbackServers(val count: Int, val items: List<ServerInfo>) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ServerInfo(
        val id: Int,
        val title: String,
        val url: String,
        val status: String
    )
}

@JsonDeserialize(using = CallbackSettingsDeserializer::class)
data class CallbackSettings(@JsonProperty("api_version") val version: String, val events: Map<String, Boolean>)

