package com.tddworks.openai.di

import com.tddworks.common.network.api.ktor.api.HttpRequester
import com.tddworks.common.network.api.ktor.internal.*
import com.tddworks.di.commonModule
import com.tddworks.openai.api.OpenAI
import com.tddworks.openai.api.OpenAIApi
import com.tddworks.openai.api.OpenAIConfig
import com.tddworks.openai.api.chat.api.Chat
import com.tddworks.openai.api.chat.internal.DefaultChatApi
import com.tddworks.openai.api.images.api.Images
import com.tddworks.openai.api.images.internal.DefaultImagesApi
import com.tddworks.openai.api.legacy.completions.api.Completions
import com.tddworks.openai.api.legacy.completions.api.internal.DefaultCompletionsApi
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initOpenAI(config: OpenAIConfig, appDeclaration: KoinAppDeclaration = {}): OpenAI {
    return startKoin {
            appDeclaration()
            modules(commonModule(false) + openAIModules(config))
        }
        .koin
        .get<OpenAI>()
}

fun openAIModules(config: OpenAIConfig) = module {
    single<OpenAI> { OpenAIApi() }

    single<HttpRequester>(named("openAIHttpRequester")) {
        HttpRequester.default(
            createHttpClient(
                connectionConfig = UrlBasedConnectionConfig(config.baseUrl),
                authConfig = AuthConfig(config.apiKey),
                // get from commonModule
                features = ClientFeatures(json = get()),
            )
        )
    }

    single<Chat> { DefaultChatApi(requester = get(named("openAIHttpRequester"))) }

    single<Images> { DefaultImagesApi(requester = get(named("openAIHttpRequester"))) }

    single<Completions> { DefaultCompletionsApi(requester = get(named("openAIHttpRequester"))) }
}
