package com.github.nikvoloshin.mirrorbot.vk.api.client.adapter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse.*

class VkResponseDeserializer<T>(private val responseClass: Class<T>) : JsonDeserializer<VkResponse<*>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VkResponse<*> {
        val mapper = jacksonObjectMapper()
        val jsonObject = p.readValueAsTree<ObjectNode>()
        return when {
            jsonObject.has("response") -> Success(mapper.readValue(jsonObject["response"].toString(), responseClass))
            jsonObject.has("error") -> mapper.readValue<Error>(jsonObject["error"].toString())
            else -> throw IllegalArgumentException("Response JSON object must have either `response` or `error` top-level field.")
        }
    }

}