package com.github.nikvoloshin.mirrorbot.bot.controller

import com.github.nikvoloshin.mirrorbot.bot.BotEventHandler
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class VkController(val eventHandler: BotEventHandler) {
    @PostMapping(consumes = ["application/json"], produces = ["text/plain"])
    fun handle(@RequestBody event: VkCallbackEvent) = eventHandler.handleEvent(event)
}