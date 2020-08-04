package com.github.nikvoloshin.mirrorbot.vk.api.client.request.method

import com.github.nikvoloshin.mirrorbot.vk.api.client.request.VkRequest

object Messages {
    fun send(userId: Int, message: String) = VkRequest<Int>(SEND)
        .withParameters("user_id" to userId, "message" to message)

    const val SEND = "messages.send"
}