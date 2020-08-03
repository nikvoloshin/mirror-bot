package com.github.nikvoloshin.mirrorbot.bot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "vk.api")
@ConstructorBinding
data class VkProperties(
    val version: String,
    val token: String,
    val groupId: String
)

@ConfigurationProperties(prefix = "bot")
@ConstructorBinding
data class BotProperties(
    val host: String,
    val title: String
)