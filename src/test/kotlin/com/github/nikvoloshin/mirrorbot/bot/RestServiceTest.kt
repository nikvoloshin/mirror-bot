package com.github.nikvoloshin.mirrorbot.bot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.nikvoloshin.mirrorbot.vk.api.client.response.VkResponse
import org.junit.jupiter.api.Assertions
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.ResponseActions
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

interface RestServiceTest {
    fun MockRestServiceServer.expectMethod(methodName: String) =
        expect { Assertions.assertEquals("/method/$methodName", it.uri.path) }

    fun ResponseActions.withQueryParameter(name: String, value: String) =
        andExpect(MockRestRequestMatchers.queryParam(name, UriUtils.encodeQuery(value, StandardCharsets.UTF_8)))

    fun ResponseActions.respondJson(obj: Any?) =
        andRespond(MockRestResponseCreators.withSuccess("{\"response\":${jacksonObjectMapper().writeValueAsString(obj)}}", MediaType.APPLICATION_JSON))

    fun ResponseActions.respondErrorJson(obj: VkResponse.Error) =
        andRespond(MockRestResponseCreators.withSuccess("{\"error\":${jacksonObjectMapper().writeValueAsString(obj)}}", MediaType.APPLICATION_JSON))
}