package com.github.nikvoloshin.mirrorbot.vk.api.client.response

import com.fasterxml.jackson.annotation.JsonProperty

sealed class VkResponse<out T> {
    class Success<out T>(val body: T) : VkResponse<T>()
    class Error(
        @JsonProperty("error_code") val code: Int,
        @JsonProperty("error_msg") val message: String
    ): VkResponse<Nothing>()
}