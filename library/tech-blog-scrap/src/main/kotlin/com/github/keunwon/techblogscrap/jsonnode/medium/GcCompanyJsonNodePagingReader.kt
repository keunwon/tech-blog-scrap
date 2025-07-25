package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

// 여기어때 기술블로그
class GcCompanyJsonNodePagingReader(
    private val queryPath: String,
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val query by lazy { BufferedReader(InputStreamReader(File(queryPath).inputStream())).use { it.readText() } }
    private val variables: MutableMap<String, Any?> = mutableMapOf(
        "postsLimit" to 10,
        "domainOrSlug" to "techblog.gccompany.co.kr",
        "tagSlug" to "tech",
        "postsFrom" to null,
    )

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(
            data = objectMapper.writeValueAsString(
                mapOf(
                    "operationName" to "PublicationTaggedQuery",
                    "query" to query,
                    "variables" to variables,
                )
            ),
            headers = mapOf("Content-type" to "application/json"),
        )
    }

    override fun doNext(node: JsonNode) {
        variables["postsFrom"] = node.getPagingFrom()!!.textValue()
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPagingFrom() != null
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val posts = node.get("data").get("collection").get("taggedPostsConnection").get("posts") as ArrayNode
        return posts.map { post ->
            post.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("extendedPreviewContent").get("subtitle").textValue(),
                    url = get("mediumUrl").textValue(),
                    authors = listOf(get("creator").get("name").textValue().split("/")[0]),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("firstPublishedAt").longValue()),
                )
            }
        }
    }

    private fun JsonNode.getPagingFrom(): JsonNode? {
        return get("data").get("collection").get("taggedPostsConnection").get("pagingInfo").get("next").get("from")
    }
}
