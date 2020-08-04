package com.github.nikvoloshin.mirrorbot.bot.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.nikvoloshin.mirrorbot.bot.BotProperties
import com.github.nikvoloshin.mirrorbot.bot.RestServiceTest
import com.github.nikvoloshin.mirrorbot.bot.VkProperties
import com.github.nikvoloshin.mirrorbot.vk.api.client.VkClient
import com.github.nikvoloshin.mirrorbot.vk.api.client.exception.VkApiException
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.method.Groups
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.ResponseActions
import org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@SpringBootTest
@ActiveProfiles("test")
class VkServiceTest(
    @Autowired val vkService: VkService,
    @Autowired vkClient: VkClient,
    @Autowired val vkProperties: VkProperties,
    @Autowired val botProperties: BotProperties
): RestServiceTest {
    private val server = MockRestServiceServer.createServer(vkClient)

    @BeforeEach
    fun setUp() = server.reset()

    @AfterEach
    fun tearDown() = server.verify()

    @Test
    fun `Obtains expected confirmation code`() {
        val expected = CallbackConfirmationCode("abcdefg")

        server.expectMethod(Groups.GET_CALLBACK_CONFIRMATION_CODE)
            .withQueryParameter("group_id", vkProperties.groupId)
            .withQueryParameter("access_token", vkProperties.token)
            .withQueryParameter("v", vkProperties.version)
            .respondJson(expected)

        val actual = vkService.getCallbackConfirmationCode()
        assertEquals(expected.code, actual)
    }

    @Test
    fun `Adds callback server`() {
        val expected = ServerId(123)
        server.expectMethod(Groups.ADD_CALLBACK_SERVER)
            .withQueryParameter("group_id", vkProperties.groupId)
            .withQueryParameter("title", botProperties.title)
            .withQueryParameter("url", botProperties.host)
            .withQueryParameter("access_token", vkProperties.token)
            .withQueryParameter("v", vkProperties.version)
            .respondJson(expected)

        val actual = vkService.addCallbackServer()
        assertEquals(expected.serverId, actual)
    }

    @Test
    fun `Gets callback servers`() {
        val expected = CallbackServers(1, listOf(
            CallbackServers.ServerInfo(123, botProperties.title, botProperties.host, "ok")
        ))

        server.expectMethod(Groups.GET_CALLBACK_SERVERS)
            .withQueryParameter("group_id", vkProperties.groupId)
            .withQueryParameter("access_token", vkProperties.token)
            .withQueryParameter("v", vkProperties.version)
            .respondJson(expected)

        val actual = vkService.getCallbackServers()
        assertEquals(expected, actual)
    }

    @Test
    fun `Gets callback settings`() {
        val serverId = 123
        val expected = CallbackSettings(vkProperties.version, mapOf("message_new" to true, "message_reply" to false))

        server.expectMethod(Groups.GET_CALLBACK_SETTINGS)
            .withQueryParameter("group_id", vkProperties.groupId)
            .withQueryParameter("server_id", serverId.toString())
            .withQueryParameter("access_token", vkProperties.token)
            .withQueryParameter("v", vkProperties.version)
            .respondJson(expected)

        val actual = vkService.getCallbackSettings(serverId)
        assertEquals(expected, actual)
    }

    @Test
    fun `Sets callback settings`() {
        val serverId = 123
        val settings = CallbackSettings(vkProperties.version, mapOf("message_new" to true, "message_reply" to false))

        server.expectMethod(Groups.SET_CALLBACK_SETTINGS)
            .withQueryParameter("group_id", vkProperties.groupId)
            .withQueryParameter("server_id", serverId.toString())
            .withQueryParameter("access_token", vkProperties.token)
            .withQueryParameter("v", vkProperties.version)
            .respondJson(1)

        val actual = vkService.setCallbackSettings(serverId, settings)
        assertEquals(1, actual)
    }

    @Test
    fun `Throws VkApiException on error response`() {
        val errorResponse = VkResponse.Error(123, "Error message")

        server.expectMethod(Groups.GET_CALLBACK_CONFIRMATION_CODE)
            .respondErrorJson(errorResponse)

        val exception = assertThrows<VkApiException> { vkService.getCallbackConfirmationCode() }
        assertEquals(errorResponse.code, exception.description.code)
        assertEquals(errorResponse.message, exception.description.message)
    }
}