package com.github.keunwon.techblogscrap.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.ApiPagingReader
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOption

class YogiyoApiPagingReader(
    override val objectMapper: ObjectMapper,
    override val apiTemplate: ApiTemplate,
) : ApiPagingReader<BlogPost>() {
    private val limit get() = pageSize
    private var to: String? = null

    @Suppress("UNCHECKED_CAST")
    private val users: Set<Map.Entry<String, JsonNode>>
        get() = currentNode.get("payload").get("references").get("User").properties()

    override fun convert(jsonNode: JsonNode): List<BlogPost> {
        val posts = jsonNode.get("payload").get("value") as ArrayNode
        return posts.map { post ->
            val user = users.find { it.key == post.get("creatorId").textValue() }?.value!!.get("name").textValue()
            BlogPost(
                title = post.get("title").textValue(),
                comment = post.get("virtuals").get("subtitle").textValue(),
                url = "https://techblog.yogiyo.co.kr/${post.get("uniqueSlug").textValue()}",
                authors = if (user != null) listOf(user) else emptyList(),
                publishedDateTime = DateTimeOption.EPOCH_MILLI.convert(post.get("createdAt").longValue()),
            )
        }
    }

    override fun next() {
        val to = currentNode.get("payload").get("paging").get("next").get("to").textValue()
        this.to = to
    }

    override fun hasNextPage(): Boolean {
        val to = currentNode.get("payload").get("paging").get("next").get("to").textValue()
        return to != null
    }

    override fun getRequestBody(): Any = mapOf(
        "limit" to limit,
        "to" to to,
    )
}
