package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MediumPublicationContentDataQueryPagingReader(
    slug: String,
    private val queryPath: String,
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val query: String by lazy {
        BufferedReader(InputStreamReader(File(queryPath).inputStream())).use { it.readText() }
    }
    private val variables: MutableMap<String, Any?> = mutableMapOf(
        "domain" to null,
        "after" to "",
        "filter" to mapOf("published" to true),
        "first" to 10,
        "orderBy" to mapOf("publishedAt" to "DESC"),
        "ref" to mapOf("slug" to slug, "domain" to null),
    )

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(
            data = objectMapper.writeValueAsString(
                mapOf(
                    "operationName" to "PublicationContentDataQuery",
                    "query" to query,
                    "variables" to variables,
                )
            ),
            headers = mapOf("Content-type" to "application/json"),
        )
    }

    override fun doNext(node: JsonNode) {
        variables["after"] = node.getPageInfo().get("endCursor").textValue()
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPageInfo().get("hasNextPage").booleanValue()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val edges = node.get("data").get("publication").get("publicationPostsConnection").get("edges")
        return edges.map { edge ->
            edge.get("node").run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("extendedPreviewContent").get("subtitle").textValue(),
                    url = get("mediumUrl").textValue(),
                    authors = get("creator").get("name").textValue().split("."),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("firstPublishedAt").longValue()),
                )
            }
        }
    }

    private fun JsonNode.getPageInfo(): JsonNode {
        return get("data").get("publication").get("publicationPostsConnection").get("pageInfo")
    }
}
