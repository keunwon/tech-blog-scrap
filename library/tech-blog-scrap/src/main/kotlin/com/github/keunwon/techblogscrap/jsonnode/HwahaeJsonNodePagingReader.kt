package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class HwahaeJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val path get() = "/_next/data/CNxbC0Mi1pZvj_9m4JUTB/category/all/tech.json?page=${page + 1}"

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://blog.hwahae.co.kr$path")
    }

    override fun doNext(node: JsonNode) {
        return
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPosts().get("pageInfo").get("offsetPagination").get("hasMore").booleanValue()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.getPosts().get("nodes").map { jsonNode ->
            jsonNode.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = "",
                    url = "https://blog.hwahae.co.kr${get("uri").textValue()}",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON.convert(get("date").textValue()),
                )
            }
        }
    }

    private fun JsonNode.getPosts(): JsonNode {
        return get("pageProps").get("dehydratedState").get("queries")[1].get("state").get("data").get("posts")
    }
}
