package com.github.nikvoloshin.mirrorbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MirrorBotApplication

fun main(args: Array<String>) {
    runApplication<MirrorBotApplication>(*args)
}
