package com.github.keunwon.techblogscrap.other

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class LineJsonNodePagingReader(
    private val apiTemplate: GetApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/page-data/ko/page/1/page-data.json"

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(path)
    }

    override fun doNext(node: JsonNode) {
        this.path = "/page-data${node.getNextPagePath()}/page-data.json"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getNextPagePath().isNotBlank()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val edges = node.get("result").get("data").get("BlogsQuery").get("edges") as ArrayNode
        return edges.map { edge ->
            edge.get("node").run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = "",
                    url = "https://techblog.lycorp.co.jp/ko/${get("landPressId").textValue()}",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.INSTANT.convert(get("pubdate").textValue()),
                )
            }
        }
    }

    private fun JsonNode.getNextPagePath(): String {
        return get("result").get("pageContext").get("nextPagePath").textValue()
    }
}
