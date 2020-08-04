package com.github.nikvoloshin.mirrorbot.vk.api.callback

import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent.Type.CONFIRMATION
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent.Type.MESSAGE_NEW
import com.github.nikvoloshin.mirrorbot.vk.api.objects.VkObject

interface VkCallbackEventHandler {
    fun handleEvent(event: VkCallbackEvent): String = when (event.eventType) {
        CONFIRMATION -> handleConfirmationRequest(event.groupId)
        MESSAGE_NEW -> handleMessageNew(event.eventObject as VkObject.Message, event.groupId)
    }

    fun handleConfirmationRequest(groupId: String): String = "ok"

    fun handleMessageNew(message: VkObject.Message, groupId: String): String = "ok"
}