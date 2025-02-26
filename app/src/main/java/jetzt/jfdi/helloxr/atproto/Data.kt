@file:OptIn(ExperimentalSerializationApi::class)


package jetzt.jfdi.skyxr.atproto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

// TODO: Respect $type with typing, not using nulls

@Serializable
data class Author(
    val did: String,
    val handle: String,
    val displayName: String,
    val avatar: String? = null,
    val labels: List<Label>,
    val createdAt: String,
    val associated: Association? = null,
)

@Serializable
data class Association(
    val chat: ChatAssociation
)

@Serializable
data class ChatAssociation(
    val allowIncoming: String
)

@Serializable
data class Reply(
    val parent: Node?,
    val root: Node?
)

@Serializable
@JsonIgnoreUnknownKeys
data class Record(
    @SerialName("\$type")
    val type: String,
    val cid: String? = null,
    val text: String,
    val createdAt: String,
    val langs: List<String>,
    val reply: Reply? = null,
    val embed: Embedded? = null,
    val facets: List<Facet>? = null,
    val uri: String? = null,
)

@Serializable
data class Facet(
    @SerialName("\$type")
    val type: String = "app.bsky.richtext.facet",
    val features: List<Feature>,
    val index: ByteSlice
)

@Serializable
data class ByteSlice(
    @SerialName("\$type")
    val type: String = "app.bsky.richtext.facet#byteSlice",
    val byteEnd: Int,
    val byteStart: Int,
)

@Serializable
data class Feature(
    @SerialName("\$type")
    val type: String = "app.bsky.richtext.facet#tag",
    val did: String? = null,
    val tag: String? = null,
    val uri: String? = null,
)

@Serializable
@JsonIgnoreUnknownKeys
data class Embedded(
    @SerialName("\$type")
    val type: String,
    val cid: String? = null,
    val images: List<EmbeddedImage>? = null,
    val playlist: String? = null,
    val video: Blob? = null,
    val external: External? = null,
    val description: String? = null,
    val title: String? = null,
    val uri: String? = null,
    val thumb: Blob? = null,
    val aspectRatio: AspectRatio? = null,
    val thumbnail: String? = null,
)

@Serializable
@JsonIgnoreUnknownKeys
data class External(
    @SerialName("\$type")
    val type: String = "app.bsky.embed.external#external",
    val title: String? = null,
    val uri: String? = null,
    val description: String? = null,
)

@Serializable
data class EmbeddedImage(
    @SerialName("\$type")
    val type: String? = null,
    val alt: String,
    val image: Blob? = null,
    val size: Int? = null,
    val thumb: String? = null,
    val fullsize: String? = null,
    val aspectRatio: AspectRatio? = null,
)

@Serializable
data class AspectRatio(
    val height: Int,
    val width: Int,
)

@Serializable
data class Blob(
    @SerialName("\$type")
    val type: String,
    @SerialName("ref")
    val reference: Reference,
    val mimeType: String,
    val size: Int? = null
)

@Serializable
data class Reference(
    @SerialName("\$link")
    val link: String
)

@Serializable
data class Node(
    val cid: String,
    val uri: String,
)

@Serializable
data class Label(
    val src: String,
    val uri: String,
    val cid: String,
    @SerialName("val")
    val value: String,
    val cts: String,
)

@Serializable
data class Post(
    val uri: String,
    val cid: String,
    val author: Author,
    val record: Record,
    val replyCount: Int,
    val repostCount: Int,
    val likeCount: Int,
    val quoteCount: Int,
    val indexedAt: String,
    val labels: List<Label>,
    val embed: Embedded? = null,
)

@Serializable
data class SearchResponse(
    val posts: List<Post>,
    val cursor: String? = null,
)
