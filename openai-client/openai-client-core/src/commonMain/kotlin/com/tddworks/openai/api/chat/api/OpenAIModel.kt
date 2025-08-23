package com.tddworks.openai.api.chat.api

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
/** https://platform.openai.com/docs/models/continuous-model-upgrades */
value class OpenAIModel(val value: String) {
    companion object {
        val GPT_3_5_TURBO = OpenAIModel("gpt-3.5-turbo")

        /**
         * New Updated GPT 3.5 Turbo The latest GPT-3.5 Turbo model with higher accuracy at
         * responding in requested formats and a fix for a bug which caused a text encoding issue
         * for non-English language function calls. Returns a maximum of 4,096 output tokens. Learn
         * more.
         */
        val GPT_3_5_TURBO_0125 = OpenAIModel("gpt-3.5-turbo-0125")

        /**
         * Similar capabilities as GPT-3 era models. Compatible with legacy Completions endpoint and
         * not Chat Completions. 4,096 tokens Up to Sep 2021
         */
        val GPT_3_5_TURBO_INSTRUCT = OpenAIModel("gpt-3.5-turbo-instruct")

        /**
         * GPT-4 Turbo with Vision The latest GPT-4 Turbo model with vision capabilities. Vision
         * requests can now use JSON mode and function calling. Currently points to
         * gpt-4-turbo-2024-04-09. 128,000 tokens Up to Dec 2023
         */
        val GPT_4_TURBO = OpenAIModel("gpt-4-turbo")

        /**
         * New GPT-4o Our most advanced, multimodal flagship model that’s cheaper and faster than
         * GPT-4 Turbo. Currently points to gpt-4o-2024-05-13.
         */
        val GPT_4O = OpenAIModel("gpt-4o")

        /**
         * GPT-4 model with the ability to understand images, in addition to all other GPT-4 Turbo
         * capabilities. This is a preview model, we recommend developers to now use gpt-4-turbo
         * which includes vision capabilities. Currently points to gpt-4-1106-vision-preview.
         */
        val GPT4_VISION_PREVIEW = OpenAIModel("gpt-4-vision-preview")
        val DALL_E_2 = OpenAIModel("dall-e-2")
        val DALL_E_3 = OpenAIModel("dall-e-3")

        /**
         * https://platform.openai.com/docs/guides/image-generation?image-generation-model=gpt-image-1
         * Image API: Generations, Edits – Responses API support coming soon Superior instruction
         * following, text rendering, detailed editing, real-world knowledge
         */
        val GPT_IMAGE_1 = OpenAIModel("gpt-image-1")

        val availableModels =
            listOf(
                GPT_3_5_TURBO,
                GPT_3_5_TURBO_0125,
                GPT_3_5_TURBO_INSTRUCT,
                GPT_4_TURBO,
                GPT_4O,
                GPT4_VISION_PREVIEW,
                DALL_E_2,
                DALL_E_3,
                GPT_IMAGE_1,
            )
    }
}
