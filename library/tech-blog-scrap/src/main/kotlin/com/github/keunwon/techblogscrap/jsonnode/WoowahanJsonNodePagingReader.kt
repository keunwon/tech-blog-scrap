package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class WoowahanJsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val request = mutableMapOf(
        "action" to "get_posts_data",
        "data[post][post_type]" to "post",
        "data[post][paged]" to "1",
        "data[meta]" to "main",
    )

    override fun fetchResponse(): Result<String> {
        val data = request.map { "${it.key}=${URLEncoder.encode(it.value, StandardCharsets.UTF_8)}" }.joinToString("&")
        return apiTemplate.fetch(
            data = data,
            headers = mapOf("Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"),
        )
    }

    override fun doNext(node: JsonNode) {
        val current = node.get("data").get("pagination").get("current").textValue().toInt()
        request["data[post][paged]"] = "${current + 1}"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        val pagination = node.get("data").get("pagination")
        val current = pagination.get("current").textValue().toInt()
        val max = pagination.get("max").intValue()
        return max > current
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val contents = node.get("data").get("posts") as ArrayNode
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = get("post_title").textValue(),
                    comment = get("excerpt").textValue(),
                    url = get("permalink").textValue(),
                    authors = get("author").get("name").textValue().split(", "),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_YY_DD_ALL_COMMA
                        .convert(get("date").textValue().replace(" ", "")),
                )
            }
        }
    }
}
