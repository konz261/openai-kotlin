package com.tddworks.gemini.api.textGeneration.api

import com.tddworks.common.network.api.ktor.internal.JsonLenient
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*

/** See https://ktor.io/docs/http-client-testing.html#usage */
fun mockHttpClient(mockResponse: String) =
    HttpClient(MockEngine) {
        val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

        install(ContentNegotiation) {
            register(ContentType.Application.Json, KotlinxSerializationConverter(JsonLenient))
        }

        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/v1beta/models/gemini-1.5-flash:generateContent" -> {
                        respond(mockResponse, HttpStatusCode.OK, headers)
                    }

                    "/v1beta/models/gemini-1.5-flash:streamGenerateContent" -> {
                        respond(mockResponse, HttpStatusCode.OK, headers)
                    }

                    else -> {
                        error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = Gemini.HOST
            }

            header(HttpHeaders.ContentType, ContentType.Application.Json)
            contentType(ContentType.Application.Json)
        }
    }
