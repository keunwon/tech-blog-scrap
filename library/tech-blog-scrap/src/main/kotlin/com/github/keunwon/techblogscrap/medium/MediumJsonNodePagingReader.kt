package com.github.keunwon.techblogscrap.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.LongNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MediumJsonNodePagingReader(
    username: String,
    queryPath: String,
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val query by lazy { BufferedReader(InputStreamReader(File(queryPath).inputStream())).use { it.readText() } }

    init {
        pageSize = 20
    }

    private val variables: MutableMap<String, Any?> =
        mutableMapOf(
            "homepagePostsFrom" to null,
            "homepagePostsLimit" to pageSize,
            "id" to null,
            "includeDistributedResponses" to true,
            "includeShouldFollowPostForExternalSearch" to true,
            "username" to username,
        )

    override fun fetchResponse(): Result<String> {
        val request = mapOf(
            "query" to query,
            "variables" to variables,
        )
        return apiTemplate.fetch(
            data = objectMapper.writeValueAsString(request),
            headers = mapOf("Content-type" to "application/json"),
        )
    }

    override fun doNext(node: JsonNode) {
        val homepagePostsFrom = node.getPagingInfo().get("next").get("from")
        require(homepagePostsFrom != null) { "homepagePostsFrom is null" }
        variables["homepagePostsFrom"] = homepagePostsFrom
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPagingInfo().get("next").get("from") != null
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val posts = node.get("data").get("userResult").get("homepagePostsConnection").get("posts") as ArrayNode
        return posts.map { post ->
            BlogPost(
                title = post.get("title").textValue(),
                comment = post.get("extendedPreviewContent").get("subtitle").textValue(),
                url = post.get("mediumUrl").textValue(),
                authors = post.get("creator").get("name").textValue().split(" "),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert((post.get("firstPublishedAt") as LongNode).longValue()),
            )
        }
    }

    private fun JsonNode.getPagingInfo(): JsonNode {
        return get("data").get("userResult").get("homepagePostsConnection").get("pagingInfo")
    }
}
