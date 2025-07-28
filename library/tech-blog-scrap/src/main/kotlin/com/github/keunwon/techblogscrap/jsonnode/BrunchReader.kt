package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class BrunchReader(
    private val domain: String,
    private val name: String,
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/v2/article/@$name?thumbnail=Y&membershipContent=false"

    override fun fetchResponse(): Result<JsonNode> = apiTemplate.get("$domain$path")

    override fun doNext(node: JsonNode) {
        val nextUrl = node.get("data").get("nextUrl").textValue()
        path = nextUrl.substring(nextUrl.indexOf("/v2/article"))
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return !node.get("data").get("nextUrl").isNull
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("data").get("list").map { element ->
            element.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("contentSummary").textValue(),
                    url = "https://brunch.co.kr/@$name/${get("no").intValue()}",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("publishTimestamp").longValue())
                )
            }
        }
    }
}
