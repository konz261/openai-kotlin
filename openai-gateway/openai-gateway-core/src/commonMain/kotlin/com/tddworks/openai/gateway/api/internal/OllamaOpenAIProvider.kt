package com.tddworks.openai.gateway.api.internal

import com.tddworks.common.network.api.ktor.api.ListResponse
import com.tddworks.ollama.api.Ollama
import com.tddworks.ollama.api.OllamaConfig
import com.tddworks.ollama.api.chat.api.toOllamaChatRequest
import com.tddworks.ollama.api.chat.api.toOllamaGenerateRequest
import com.tddworks.ollama.api.chat.api.toOpenAIChatCompletion
import com.tddworks.ollama.api.chat.api.toOpenAIChatCompletionChunk
import com.tddworks.ollama.api.chat.api.toOpenAICompletion
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
class OllamaOpenAIProvider(
    override val id: String = "ollama",
    override val name: String = "Ollama",
    override val config: OllamaOpenAIProviderConfig,
    private val client: Ollama =
        Ollama.create(ollamaConfig = OllamaConfig(baseUrl = config.baseUrl)),
) : OpenAIProvider {

    /**
     * Override function to fetch completions from OpenAI API based on the given
     * ChatCompletionRequest
     *
     * @param request the ChatCompletionRequest object containing information needed to generate
     *   completions
     * @return OpenAIChatCompletion object containing completions generated from the OpenAI API
     */
    override suspend fun chatCompletions(request: ChatCompletionRequest): ChatCompletion {
        val ollamaChatRequest = request.toOllamaChatRequest()
        return client.request(ollamaChatRequest).toOpenAIChatCompletion()
    }

    /**
     * A function that streams completions for chat based on the given ChatCompletionRequest
     *
     * @param request The ChatCompletionRequest containing the request details
     * @return A Flow of OpenAIChatCompletionChunk objects representing the completions
     */
    override fun streamChatCompletions(request: ChatCompletionRequest): Flow<ChatCompletionChunk> {
        return client.stream(request.toOllamaChatRequest()).transform {
            emit(it.toOpenAIChatCompletionChunk())
        }
    }

    override suspend fun completions(request: CompletionRequest): Completion {
        return client.request(request.toOllamaGenerateRequest()).toOpenAICompletion()
    }

    override suspend fun generate(request: ImageCreate): ListResponse<Image> {
        TODO("Not yet implemented")
    }
}

fun OpenAIProvider.Companion.ollama(
    id: String = "ollama",
    config: OllamaOpenAIProviderConfig,
    client: Ollama = Ollama.create(ollamaConfig = OllamaConfig(baseUrl = config.baseUrl)),
): OpenAIProvider {
    return OllamaOpenAIProvider(id = id, config = config, client = client)
}
