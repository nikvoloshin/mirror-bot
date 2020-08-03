package com.github.nikvoloshin.mirrorbot.vk.api.client.exception

import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse

class VkApiException(val description: VkResponse.Error)
    : Exception("Error code ${description.code}: ${description.message}")