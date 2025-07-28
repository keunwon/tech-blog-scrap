package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class KakaomobilityJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var query = "?itemCount=10&pageIndex=1"

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://developers.kakaomobility.com/api/techblogs$query")
    }

    override fun doNext(node: JsonNode) {
        val nextPage = node.get("search").get("pageIndex").intValue() + 1
        query = "?itemCount=10&pageIndex=${nextPage}"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        val search = node.get("data").get("search")
        val endPage = search.get("endPage").intValue()
        val curPage = search.get("pageIndex").intValue()
        return curPage < endPage
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("data").get("list").map { nd ->
            nd.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("summary").textValue(),
                    url = get("link").textValue(),
                    authors = get("writerName").textValue().replace(" ", "").split(", "),
                    categories = get("tags").map { it.textValue() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON
                        .convert(get("createAt").textValue().replace(" ", "T")),
                )
            }
        }
    }
}
