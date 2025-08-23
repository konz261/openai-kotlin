package com.tddworks.openai.gateway.api.internal

import com.tddworks.common.network.api.ktor.api.ListResponse
import com.tddworks.gemini.api.textGeneration.api.*
import com.tddworks.openai.api.chat.api.ChatCompletion
import com.tddworks.openai.api.chat.api.ChatCompletionChunk
import com.tddworks.openai.api.chat.api.ChatCompletionRequest
import com.tddworks.openai.api.images.api.Image
import com.tddworks.openai.api.images.api.ImageCreate
import com.tddworks.openai.api.legacy.completions.api.Completion
import com.tddworks.openai.api.legacy.completions.api.CompletionRequest
import com.tddworks.openai.gateway.api.OpenAIProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
class GeminiOpenAIProvider(
    override val id: String = "gemini",
    override val name: String = "Gemini",
    override val config: GeminiOpenAIProviderConfig,
    val client: Gemini,
) : OpenAIProvider {

    override suspend fun chatCompletions(request: ChatCompletionRequest): ChatCompletion {
        val geminiRequest = request.toGeminiGenerateContentRequest()
        return client.generateContent(geminiRequest).toOpenAIChatCompletion()
    }

    override fun streamChatCompletions(request: ChatCompletionRequest): Flow<ChatCompletionChunk> {
        val geminiRequest = request.toGeminiGenerateContentRequest().copy(stream = true)

        return client.streamGenerateContent(geminiRequest).transform {
            emit(it.toOpenAIChatCompletionChunk())
        }
    }

    override suspend fun completions(request: CompletionRequest): Completion {
        throw UnsupportedOperationException("Not supported")
    }

    override suspend fun generate(request: ImageCreate): ListResponse<Image> {
        throw UnsupportedOperationException("Not supported")
    }
}

fun OpenAIProvider.Companion.gemini(
    id: String = "gemini",
    config: GeminiOpenAIProviderConfig,
    client: Gemini,
): OpenAIProvider {
    return GeminiOpenAIProvider(id = id, config = config, client = client)
}
