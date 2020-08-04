package com.github.nikvoloshin.mirrorbot

import com.github.nikvoloshin.mirrorbot.bot.BotProperties
import com.github.nikvoloshin.mirrorbot.bot.ConfirmationCodeHolder
import com.github.nikvoloshin.mirrorbot.bot.VkProperties
import com.github.nikvoloshin.mirrorbot.bot.service.VkService
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class MirrorBotApplicationRunner(
    private val vkService: VkService,
    private val confirmationCodeHolder: ConfirmationCodeHolder,
    private val botProperties: BotProperties,
    private val vkProperties: VkProperties
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        obtainConfirmationCode()
        ensureCallbackServer()
    }

    private fun obtainConfirmationCode() {
        val code = vkService.getCallbackConfirmationCode()
        confirmationCodeHolder.code = code
    }

    private fun ensureCallbackServer() {
        val registeredServers = vkService.getCallbackServers()
        val serverId = registeredServers.items.find { it.url == botProperties.host }?.id
            ?: vkService.addCallbackServer()

        val serverSettings = vkService.getCallbackSettings(serverId)
        val newEvents = serverSettings.events.toMutableMap().also { it[VkCallbackEvent.Type.MESSAGE_NEW.value] = true }

        val newSettings = CallbackSettings(vkProperties.version, newEvents)
        vkService.setCallbackSettings(serverId, newSettings)
    }

}