package com.github.nikvoloshin.mirrorbot.vk.api.objects

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

sealed class VkObject {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Message(
        @JsonProperty("from_id")
        val userId: Int,
        val text: String
    ) : VkObject()
}