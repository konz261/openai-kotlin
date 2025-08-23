package com.tddworks.openai.api.chat.api

import com.tddworks.openai.api.chat.api.vision.ImageUrl
import com.tddworks.openai.api.chat.api.vision.VisionMessageContent
import com.tddworks.openai.api.common.prettyJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatMessageTest {

    @Test
    fun `should return correct json for vision message`() {
        val message =
            ChatMessage.vision(
                listOf(
                    VisionMessageContent.TextContent(content = "What is the name of this flower?"),
                    VisionMessageContent.ImageContent(
                        imageUrl = ImageUrl(value = "https://example.com/image.jpg")
                    ),
                )
            )

        val expectedJson =
            """
        {
          "content": [
            {
              "type": "text",
              "text": "What is the name of this flower?"
            },
            {
              "type": "image_url",
              "image_url": {
                "url": "https://example.com/image.jpg",
                "detail": "auto"
              }
            }
          ],
          "role": "user"
        }
        """
                .trimIndent()

        assertEquals(
            expectedJson,
            prettyJson.encodeToString(ChatMessage.VisionMessage.serializer(), message),
        )
    }

    @Test
    fun `should return correct json for assistant message`() {
        val message = ChatMessage.assistant("Hello, how may I assist you today?")

        val expectedJson =
            """
            {
              "content": "Hello, how may I assist you today?",
              "role": "assistant"
            }
        """
                .trimIndent()

        assertEquals(
            expectedJson,
            prettyJson.encodeToString(ChatMessage.AssistantMessage.serializer(), message),
        )
    }

    @Test
    fun `should return correct json for user message`() {
        val message = ChatMessage.user("Hello, how may I assist you today?")

        val expectedJson =
            """
            {
              "content": "Hello, how may I assist you today?",
              "role": "user"
            }
        """
                .trimIndent()

        assertEquals(
            expectedJson,
            prettyJson.encodeToString(ChatMessage.UserMessage.serializer(), message),
        )
    }

    @Test
    fun `should return correct json for system message`() {
        val message = ChatMessage.system("Hello, how may I assist you today?")

        val expectedJson =
            """
            {
              "content": "Hello, how may I assist you today?",
              "role": "system"
            }
        """
                .trimIndent()

        assertEquals(
            expectedJson,
            prettyJson.encodeToString(ChatMessage.SystemMessage.serializer(), message),
        )
    }
}
