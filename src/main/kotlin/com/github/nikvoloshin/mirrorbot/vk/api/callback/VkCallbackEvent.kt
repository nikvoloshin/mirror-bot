package com.github.nikvoloshin.mirrorbot.vk.api.callback

import com.github.nikvoloshin.mirrorbot.vk.api.objects.VkObject

data class VkCallbackEvent(
    val eventType: Type,
    val eventObject: VkObject?,
    val groupId: String
) {
    enum class Type(val value: String) {
        CONFIRMATION("confirmation"),
        MESSAGE_NEW("message_new")
    }
}
