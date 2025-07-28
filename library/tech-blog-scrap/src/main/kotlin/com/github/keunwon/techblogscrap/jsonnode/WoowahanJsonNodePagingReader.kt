package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class WoowahanJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val formRequest = mutableMapOf(
        "action" to "get_posts_data",
        "data[post][post_type]" to "post",
        "data[post][paged]" to "1",
        "data[meta]" to "main",
    )

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.postForm(
            url = "https://techblog.woowahan.com/wp-admin/admin-ajax.php",
            form = formRequest,
            headers = mapOf("Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8")
        )
    }

    override fun doNext(node: JsonNode) {
        val current = node.get("data").get("pagination").get("current").textValue().toInt()
        formRequest["data[post][paged]"] = "${current + 1}"
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
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA
                        .convert(get("date").textValue().replace(" ", "").dropLast(1)),
                )
            }
        }
    }
}
