package com.github.nikvoloshin.mirrorbot.vk.api.callback.adapter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent.Type.*
import com.github.nikvoloshin.mirrorbot.vk.api.objects.VkObject
import org.springframework.boot.jackson.JsonComponent
import java.lang.IllegalArgumentException

@JsonComponent
class VkCallbackEventDeserializer : JsonDeserializer<VkCallbackEvent>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): VkCallbackEvent {
        val jsonObject = p.readValueAsTree<ObjectNode>()

        val typeString = jsonObject.required("type").asText()
        val groupId = jsonObject.required("group_id").asText()

        if (typeString == CONFIRMATION.value) {
            return VkCallbackEvent(CONFIRMATION, null, groupId)
        }

        return when (typeString) {
            MESSAGE_NEW.value -> prepareEvent(MESSAGE_NEW, groupId, jsonObject, VkObject.Message::class.java)
            else -> throw IllegalArgumentException("Unknown callback event type: $typeString")
        }
    }

    private fun <T: VkObject> prepareEvent(
        eventType: VkCallbackEvent.Type,
        groupId: String,
        jsonObject: ObjectNode,
        clazz: Class<T>
    ): VkCallbackEvent {
        val vkObject = jacksonObjectMapper().readValue(jsonObject.required("object").toString(), clazz)
        return VkCallbackEvent(eventType, vkObject, groupId)
    }
}