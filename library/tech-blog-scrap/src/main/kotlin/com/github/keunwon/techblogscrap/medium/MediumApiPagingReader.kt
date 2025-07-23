package com.github.keunwon.techblogscrap.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.LongNode
import com.github.keunwon.techblogscrap.ApiPagingReader
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOption

class MediumApiPagingReader(
    val username: String,
    val query: String,
    override val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : ApiPagingReader<BlogPost>() {
    private val variables: MutableMap<String, Any?> by lazy {
        mutableMapOf(
            "homepagePostsFrom" to null,
            "homepagePostsLimit" to 20,
            "id" to null,
            "includeDistributedResponses" to true,
            "includeShouldFollowPostForExternalSearch" to true,
            "username" to username,
        )
    }

    override fun convert(jsonNode: JsonNode): List<BlogPost> {
        val posts = jsonNode.get("data").get("userResult").get("homepagePostsConnection").get("posts") as ArrayNode
        return posts.map { post ->
            BlogPost(
                title = post.get("title").textValue(),
                comment = post.get("extendedPreviewContent").get("subtitle").textValue(),
                url = post.get("mediumUrl").textValue(),
                authors = post.get("creator").get("name").textValue().split(" "),
                publishedDateTime = DateTimeOption.EPOCH_MILLI.convert((post.get("firstPublishedAt") as LongNode).longValue()),
            )
        }
    }

    override fun next() {
        val homepagePostsFrom = currentNode.getPagingInfo().get("next").get("from")
        require(homepagePostsFrom != null) { "homepagePostsFrom is null" }
        variables["homepagePostsFrom"] = homepagePostsFrom
    }

    override fun hasNextPage(): Boolean {
        return currentNode.getPagingInfo().get("next").get("from") != null
    }

    override fun getRequestBody(): Any = mapOf(
        "query" to query,
        "variables" to variables,
    )

    private fun JsonNode.getPagingInfo(): JsonNode {
        return get("data").get("userResult").get("homepagePostsConnection").get("pagingInfo")
    }
}
