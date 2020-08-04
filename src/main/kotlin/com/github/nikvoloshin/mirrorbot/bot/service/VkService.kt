package com.github.nikvoloshin.mirrorbot.bot.service

import com.github.nikvoloshin.mirrorbot.bot.BotProperties
import com.github.nikvoloshin.mirrorbot.bot.VkProperties
import com.github.nikvoloshin.mirrorbot.vk.api.client.VkClient
import com.github.nikvoloshin.mirrorbot.vk.api.client.exception.VkApiException
import com.github.nikvoloshin.mirrorbot.vk.api.client.executeRequest
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.VkRequest
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.method.Groups
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.method.Messages
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackSettings
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse
import org.springframework.stereotype.Service

@Service
class VkService(
    private val vkClient: VkClient,
    private val vkProperties: VkProperties,
    private val botProperties: BotProperties
) {
    fun getCallbackConfirmationCode() =
        vkClient.executeRequest(Groups.getCallbackConfirmationCode(vkProperties.groupId)).code

    fun addCallbackServer() =
        vkClient.executeRequest(Groups.addCallbackServer(vkProperties.groupId, botProperties.host, botProperties.title)).serverId

    fun getCallbackServers() =
        vkClient.executeRequest(Groups.getCallbackServers(vkProperties.groupId))

    fun getCallbackSettings(serverId: Int) =
        vkClient.executeRequest(Groups.getCallbackSettings(vkProperties.groupId, serverId))

    fun setCallbackSettings(serverId: Int, settings: CallbackSettings) =
        vkClient.executeRequest(Groups.setCallbackSettings(vkProperties.groupId, serverId, settings))

    fun sendMessage(userId: Int, message: String) =
        vkClient.executeRequest(Messages.send(userId, message))

    private inline fun <reified R> VkClient.executeRequest(request: VkRequest<R>): R {
        when (val response = executeRequest(request, vkProperties.token, vkProperties.version)) {
            is VkResponse.Success -> return response.body
            is VkResponse.Error -> throw VkApiException(response)
        }
    }
}