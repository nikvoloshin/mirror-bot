package com.github.nikvoloshin.mirrorbot.vk.api.client.adapter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackSettings
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class CallbackSettingsDeserializer : JsonDeserializer<CallbackSettings>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): CallbackSettings {
        val jsonObject = p.readValueAsTree<ObjectNode>()

        val version = jsonObject.required("api_version").asText()
        val events = jsonObject.required("events").fields().asSequence()
            .map { it.key to it.value.asBoolean()}

        return CallbackSettings(version, events.toMap())
    }
}