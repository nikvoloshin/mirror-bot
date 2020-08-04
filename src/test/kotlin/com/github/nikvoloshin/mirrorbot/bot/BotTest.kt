package com.github.nikvoloshin.mirrorbot.bot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.nikvoloshin.mirrorbot.bot.service.VkService
import com.github.nikvoloshin.mirrorbot.vk.api.callback.VkCallbackEvent
import com.github.nikvoloshin.mirrorbot.vk.api.client.VkClient
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.method.Groups
import com.github.nikvoloshin.mirrorbot.vk.api.client.request.method.Messages
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.CallbackConfirmationCode
import com.github.nikvoloshin.mirrorbot.vk.api.objects.VkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.postForEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BotTest(
    @Autowired private val vkService: VkService,
    @Autowired private val vkClient: VkClient,
    @Autowired private val vkProperties: VkProperties
) : RestServiceTest {

    @LocalServerPort
    private var localPort: Int = -1
    private val localhostUrl get() = "http://localhost:$localPort/"

    private val server = MockRestServiceServer.createServer(vkClient)
    private val restTemplate = TestRestTemplate()

    @AfterEach
    fun setUp() = server.reset()

    @BeforeEach
    fun tearDown() = server.verify()

    @Test
    fun `Controller returns correct confirmation code`(@Autowired confirmationCodeHolder: ConfirmationCodeHolder) {
        val expectedCode = CallbackConfirmationCode("code")
        server.expectMethod(Groups.GET_CALLBACK_CONFIRMATION_CODE)
            .respondJson(expectedCode)

        val code = vkService.getCallbackConfirmationCode()
        confirmationCodeHolder.code = code

        val confirmationEvent = VkCallbackEvent(VkCallbackEvent.Type.CONFIRMATION, null, vkProperties.groupId)
        val response = restTemplate.sendEvent(confirmationEvent)

        assertEquals(response.statusCode, HttpStatus.OK)
        assertEquals(response.body, expectedCode.code)
    }

    @Test
    fun `Client replies with the same message`() {
        val message = VkObject.Message(123, "message")

        server.expectMethod(Messages.SEND)
            .withQueryParameter("user_id", message.userId.toString())
            .withQueryParameter("message", message.text)
            .respondJson(1)

        val response = restTemplate.sendEvent(VkCallbackEvent(VkCallbackEvent.Type.MESSAGE_NEW, message, vkProperties.groupId))
        assertEquals(response.statusCode, HttpStatus.OK)
        assertEquals(response.body, "ok")
    }

    private fun TestRestTemplate.sendEvent(event: VkCallbackEvent): ResponseEntity<String> {
        val typeString = """ "type": "${event.eventType.value}", """
        val objectString = event.eventObject?.let { """ "object": ${jacksonObjectMapper().writeValueAsString(it)}, """ }
            ?: ""
        val groupIdString = """ "group_id": ${event.groupId} """

        val eventJson = """
            {
                $typeString
                $objectString
                $groupIdString
            }
        """.trimIndent()

        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity(eventJson, headers)

        return restTemplate.postForEntity(localhostUrl, request)
    }
}