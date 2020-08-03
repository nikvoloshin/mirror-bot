package com.github.nikvoloshin.mirrorbot.vk.api.client.request.method

import com.github.nikvoloshin.mirrorbot.vk.api.client.request.VkRequest
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackConfirmationCode
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackServers
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackSettings
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.ServerId

object Groups {
    fun getCallbackConfirmationCode(groupId: String) = VkRequest<CallbackConfirmationCode>(GET_CALLBACK_CONFIRMATION_CODE)
        .withParameters("group_id" to groupId)

    fun addCallbackServer(groupId: String, url: String, title: String) = VkRequest<ServerId>(ADD_CALLBACK_SERVER)
        .withParameters("group_id" to groupId, "url" to url, "title" to title)

    fun getCallbackServers(groupId: String) = VkRequest<CallbackServers>(GET_CALLBACK_SERVERS)
        .withParameters("group_id" to groupId)

    fun getCallbackSettings(groupId: String, serverId: Int) = VkRequest<CallbackSettings>(GET_CALLBACK_SETTINGS)
        .withParameters("group_id" to groupId, "server_id" to serverId)

    fun setCallbackSettings(groupId: String, serverId: Int, settings: CallbackSettings) = VkRequest<Int>(SET_CALLBACK_SETTINGS)
        .withParameters("group_id" to groupId, "server_id" to serverId, "api_version" to settings.version)
        .withParameters(*settings.events.mapValues { if (it.value) 1 else 0 }.toList().toTypedArray())


    const val GET_CALLBACK_CONFIRMATION_CODE = "groups.getCallbackConfirmationCode"
    const val ADD_CALLBACK_SERVER = "groups.addCallbackServer"
    const val GET_CALLBACK_SERVERS = "groups.getCallbackServers"
    const val GET_CALLBACK_SETTINGS = "groups.getCallbackSettings"
    const val SET_CALLBACK_SETTINGS = "groups.setCallbackSettings"
}