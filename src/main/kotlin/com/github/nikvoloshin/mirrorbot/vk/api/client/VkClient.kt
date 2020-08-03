package com.github.nikvoloshin.mirrorbot.vk.api.client

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nikvoloshin.mirrorbot.vk.api.client.adapter.VkResponseDeserializer
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.VkRequest
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class VkClient : RestTemplate() {
    fun <T> executeRequest(
        request: VkRequest<T>,
        accessToken: String? = null,
        version: String,
        responseClass: Class<T>
    ): VkResponse<T> {
        val url = buildUrl(prepareRequest(request, accessToken, version))
        val json = getForObject<String>(url)
        return deserializeResponse(json, responseClass)
    }

    private fun <T> prepareRequest(request: VkRequest<T>, accessToken: String?, version: String) = request.apply {
        if (accessCodeRequired) {
            withParameters("access_token" to accessToken)
        }
        withParameters("v" to version)
    }

    private fun buildUrl(request: VkRequest<*>): URI {
        val uriBuilder = UriComponentsBuilder.fromUriString(VK_API_ADDRESS).path(request.methodName)
        request.parameters.forEach { uriBuilder.queryParam(it.key, it.value) }
        return uriBuilder.build().toUri()
    }

    private fun <T> deserializeResponse(json: String, responseClass: Class<T>): VkResponse<T> {
        val deserModule = SimpleModule().addDeserializer(VkResponse::class.java, VkResponseDeserializer(responseClass))
        return jacksonObjectMapper().registerModule(deserModule).readValue(json)
    }

    companion object {
        const val VK_API_ADDRESS = "https://api.vk.com/method/"
    }
}

inline fun <reified T> VkClient.executeRequest(
    request: VkRequest<T>,
    accessToken: String? = null,
    version: String
) = executeRequest(request, accessToken, version, T::class.java)
