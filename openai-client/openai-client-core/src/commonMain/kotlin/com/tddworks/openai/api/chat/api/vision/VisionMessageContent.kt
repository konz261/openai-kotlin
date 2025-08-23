@file:OptIn(ExperimentalSerializationApi::class)

package com.tddworks.openai.api.chat.api.vision

import kotlin.jvm.JvmInline
import kotlinx.serialization.*

@Serializable(with = VisionMessageContentSerializer::class)
sealed interface VisionMessageContent {
    val type: ContentType

    @Serializable
    data class TextContent(
        /**
         * Controls whether the target property is serialized when its value is equal to a default
         * value, regardless of the format settings. Does not affect decoding and deserialization
         * process.
         *
         * Example of usage:
         * ```
         * @Serializable
         * data class Foo(
         *     @EncodeDefault(ALWAYS) val a: Int = 42,
         *     @EncodeDefault(NEVER) val b: Int = 43,
         *     val c: Int = 44
         * )
         *
         * Json { encodeDefaults = false }.encodeToString((Foo()) // {"a": 42}
         * Json { encodeDefaults = true }.encodeToString((Foo())  // {"a": 42, "c":44}
         * ```
         *
         * @see EncodeDefault.Mode.ALWAYS
         * @see EncodeDefault.Mode.NEVER
         */
        @EncodeDefault(EncodeDefault.Mode.ALWAYS)
        @SerialName("type")
        override val type: ContentType = ContentType.TEXT,
        @SerialName("text") val content: String,
    ) : VisionMessageContent

    @Serializable
    data class ImageContent(
        @EncodeDefault(EncodeDefault.Mode.ALWAYS)
        @SerialName("type")
        override val type: ContentType = ContentType.IMAGE,
        @SerialName("image_url") val imageUrl: ImageUrl,
    ) : VisionMessageContent
}

/**
 * Represents an image URL with associated details for serialization.
 *
 * This data class is used to serialize and deserialize image URL information, including the URL
 * itself and additional details as an enumeration. It's annotated for use with Kotlin
 * Serialization.
 *
 * @property value The URL of the image. This is represented as a [String].
 * @property detail An additional detail about the image URL. This defaults to [ImageUrlDetail.AUTO]
 *   and is always encoded in serialized format.
 *
 * Example JSON structure for using with the Chat Completions API.
 *
 * ```
 * {
 *     "role": "user",
 *     "content": [
 *         {
 *             "type": "text",
 *             "text": "What is in this image?"
 *         },
 *         {
 *             "type": "image_url",
 *             "image_url": {"url": "data:image/jpeg;base64,[base64_image]"}
 *         },
 *         {
 *             "type": "image_url",
 *             "image_url": {"url": "https://example.com/image.jpg"}
 *         }
 *     ]
 * }
 * ```
 *
 * @see
 *   [OpenAI Vision Documentation](https://platform.openai.com/docs/guides/vision#uploading-base64-encoded-images)
 *   The Chat Completions API is capable of taking in and processing multiple image inputs in both
 *   base64 encoded format or as an image URL.
 */
@Serializable
data class ImageUrl(
    @SerialName("url") val value: String,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("detail")
    val detail: ImageUrlDetail = ImageUrlDetail.AUTO,
)

/**
 * By controlling the detail parameter, which has three options, low, high, or auto, you have
 * control over how the model processes the image and generates its textual understanding. By
 * default, the model will use the auto setting which will look at the image input size and decide
 * if it should use the low or high setting.
 */
@Serializable
@JvmInline
value class ImageUrlDetail(val value: String) {
    companion object {
        val AUTO = ImageUrlDetail("auto")

        /**
         * low will disable the “high res” model. The model will receive a low-res 512px x 512px
         * version of the image, and represent the image with a budget of 65 tokens. This allows the
         * API to return faster responses and consume fewer input tokens for use cases that do not
         * require high detail
         */
        val LOW = ImageUrlDetail("low")

        /**
         * high will enable “high res” mode, which first allows the model to see the low res image
         * and then creates detailed crops of input images as 512px squares based on the input image
         * size. Each of the detailed crops uses twice the token budget (65 tokens) for a total of
         * 129 tokens.
         */
        val HIGH = ImageUrlDetail("high")
    }
}

@Serializable
@JvmInline
value class ContentType(val value: String) {
    companion object {
        val TEXT = ContentType("text")
        val IMAGE = ContentType("image_url")
    }
}
