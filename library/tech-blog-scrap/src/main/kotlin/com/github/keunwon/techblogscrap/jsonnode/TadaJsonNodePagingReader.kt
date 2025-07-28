package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class TadaJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/page-data/index/page-data.json"

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://blog-tech.tadatada.com/$path")
    }

    override fun doNext(node: JsonNode) {
        path = "/page-data/page/${page + 1}/page-data.json"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        val current = node.get("result").get("pageContext").get("currentPageNumber").intValue()
        val pageCount = node.get("result").get("pageContext").get("pageCount").intValue()
        return current < pageCount
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("result").get("data").get("allMarkdownRemark").get("nodes").map { jsonNode ->
            jsonNode.run {
                BlogPost(
                    title = get("post").get("title").textValue(),
                    comment = "",
                    url = "https://blog-tech.tadatada.com${get("post").get("permalink").textValue()}",
                    authors = emptyList(),
                    categories = get("post").get("tags").map { it.textValue() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON
                        .convert(get("post").get("date").textValue().take(19)),
                )
            }
        }
    }
}
