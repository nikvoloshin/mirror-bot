package com.github.nikvoloshin.mirrorbot.bot

import org.springframework.stereotype.Component
import kotlin.properties.Delegates

@Component
class ConfirmationCodeHolder {
    var code: String by Delegates.notNull()
}

@Component
class ServerIdHolder {
    var serverId: Int by Delegates.notNull()
}