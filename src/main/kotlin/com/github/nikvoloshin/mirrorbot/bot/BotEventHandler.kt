package com.github.nikvoloshin.mirrorbot.bot

import com.github.nikvoloshin.mirrorbot.bot.service.VkService
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEventHandler
import com.github.nikvoloshin.mirrorbot.vk.api.objects.VkObject
import org.springframework.stereotype.Component

@Component
class BotEventHandler(
    val confirmationCodeHolder: ConfirmationCodeHolder,
    val vkService: VkService
) : VkCallbackEventHandler {

    override fun handleConfirmationRequest(groupId: String): String {
        return confirmationCodeHolder.code
    }

    override fun handleMessageNew(message: VkObject.Message, groupId: String): String {
        vkService.sendMessage(message.userId, message.text)
        return "ok"
    }
}