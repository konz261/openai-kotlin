package com.tddworks.openai.api.chat.internal

import app.cash.turbine.test
import com.tddworks.common.network.api.ktor.internal.DefaultHttpRequester
import com.tddworks.di.initKoin
import com.tddworks.openai.api.chat.api.*
import com.tddworks.openai.api.common.mockHttpClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.junit5.AutoCloseKoinTest

@OptIn(ExperimentalSerializationApi::class)
class DefaultChatApiTest : AutoCloseKoinTest() {

    @BeforeEach
    fun setUp() {
        initKoin()
    }

    @Test
    fun `should return chat completion`() = runBlocking {
        val request =
            ChatCompletionRequest.chatCompletionsRequest(listOf(ChatMessage.UserMessage("hello")))

        val chat =
            DefaultChatApi(
                requester =
                    DefaultHttpRequester(
                        httpClient =
                            mockHttpClient(
                                """
                    {
                      "id": "chatcmpl-8Zu4AF8QMK3zFgdzXIPjFS4VkWErX",
                      "object": "chat.completion",
                      "created": 1703567690,
                      "model": "gpt-3.5-turbo-0613",
                      "choices": [
                        {
                          "index": 0,
                          "message": {
                            "role": "assistant",
                            "content": "Hello! How can I assist you today?"
                          },
                          "logprobs": null,
                          "finish_reason": "stop"
                        }
                      ],
                      "usage": {
                        "prompt_tokens": 8,
                        "completion_tokens": 9,
                        "total_tokens": 17
                      },
                      "system_fingerprint": null
                    }
                """
                                    .trimIndent()
                            )
                    )
            )

        val r = chat.chatCompletions(request)

        with(r) {
            assertEquals("chatcmpl-8Zu4AF8QMK3zFgdzXIPjFS4VkWErX", id)
            assertEquals(1703567690, created)
            assertEquals("gpt-3.5-turbo-0613", model)
            assertEquals(1, choices.size)
            assertEquals("Hello! How can I assist you today?", choices[0].message.content)
            assertEquals(0, choices[0].index)
        }
    }

    @Test
    fun `should return stream of completions`() = runBlocking {
        val request =
            ChatCompletionRequest.chatCompletionsRequest(listOf(ChatMessage.UserMessage("hello")))

        val chat =
            DefaultChatApi(
                requester =
                    DefaultHttpRequester(
                        httpClient =
                            mockHttpClient(
                                "data: {\"id\":\"chatcmpl-8ZtRSZzsijxilL2lDBN7ERQc0Zi7Q\",\"object\":\"chat.completion.chunk\",\"created\":1703565290,\"model\":\"gpt-3.5-turbo-0613\",\"system_fingerprint\":null,\"choices\":[{\"index\":0,\"delta\":{\"content\":\" there\"},\"logprobs\":null,\"finish_reason\":null}]}"
                            )
                    )
            )

        chat.streamChatCompletions(request).test {
            assertEquals(
                ChatCompletionChunk(
                    id = "chatcmpl-8ZtRSZzsijxilL2lDBN7ERQc0Zi7Q",
                    `object` = "chat.completion.chunk",
                    created = 1703565290,
                    model = "gpt-3.5-turbo-0613",
                    choices =
                        listOf(
                            ChatChunk(
                                index = 0,
                                delta = ChatDelta(content = " there"),
                                finishReason = null,
                            )
                        ),
                ),
                awaitItem(),
            )
            awaitComplete()
        }
    }
}
