package com.github.nikvoloshin.mirrorbot.vk.api.client.request

class VkRequest<R>(val methodName: String, val accessCodeRequired: Boolean = true) {
    val parameters: Map<String, Any> get() = _parameters
    private val _parameters = mutableMapOf<String, Any>()

    fun withParameters(vararg parameters: Pair<String, Any?>) = this.apply {
        parameters.filter { it.second != null }.forEach { _parameters += it.first to it.second!!.toString() }
    }
}