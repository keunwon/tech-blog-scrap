package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class YogiyoJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val limit get() = pageSize
    private var to: String? = null

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.post(
            url = "https://medium.com/deliverytechkorea/load-more?sortBy=latest",
            data = mapOf(
                "limit" to limit,
                "to" to to,
            ),
            headers = mapOf(
                "x-xsrf-token" to "1",
                "Accept" to "application/json",
            )
        )
    }

    override fun doNext(node: JsonNode) {
        val to = node.get("payload").get("paging").get("next").get("to").textValue()
        this.to = to
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        val to = node.get("payload").get("paging").get("next").get("to").textValue()
        return to != null
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val posts = node.get("payload").get("value") as ArrayNode
        val users = node.get("payload").get("references").get("User")?.properties() ?: emptySet()

        return posts.map { post ->
            val user = users.find { it.key == post.get("creatorId").textValue() }?.value!!.get("name").textValue()
            BlogPost(
                title = post.get("title").textValue(),
                comment = post.get("virtuals").get("subtitle").textValue(),
                url = "https://techblog.yogiyo.co.kr/${post.get("uniqueSlug").textValue()}",
                authors = if (user != null) listOf(user) else emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(post.get("createdAt").longValue()),
            )
        }
    }
}
