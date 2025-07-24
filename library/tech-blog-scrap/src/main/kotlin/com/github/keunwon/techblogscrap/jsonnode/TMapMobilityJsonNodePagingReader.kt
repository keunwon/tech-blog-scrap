package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class TMapMobilityJsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var query = "?thumbnail=Y&membershipContent=false"

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(query)
    }

    override fun doNext(node: JsonNode) {
        val (_, query) = node.get("data").get("nextUrl").textValue().split("?")
        this.query = "?$query"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return !node.get("data").get("nextUrl").isNull
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("data").get("list").map { jn ->
            jn.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("subTitle").textValue(),
                    url = "https://brunch.co.kr/@tmapmobility/${get("no").intValue()}",
                    authors = listOf(),
                    categories = listOf(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("publishTimestamp").longValue()),
                )
            }
        }
    }
}
