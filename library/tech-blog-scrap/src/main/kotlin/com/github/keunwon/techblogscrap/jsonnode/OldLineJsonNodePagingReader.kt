package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class OldLineJsonNodePagingReader(
    private val apiTemplate: GetApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/page-data/ko/blog/page-data.json"

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(path)
    }

    override fun doNext(node: JsonNode) {
        val page = node.get("result").get("pageContext").get("humanPageNumber").intValue() + 1
        path = "/page-data/ko/blog/page/$page/page-data.json"
    }

    override fun doHasNextPage(node: JsonNode): Boolean = !node.getEdges().isEmpty

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.getEdges().map { edge ->
            edge.get("node").run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("content").textValue().replace(HTML_TAG_REGEX, ""),
                    url = "https://engineering.linecorp.com/ko/blog/${get("slug").textValue()}",
                    authors = get("authors").get("display_name").textValue().split(" / "),
                    publishedDateTime = DateTimeOptions.INSTANT.convert(get("pubdate").textValue()),
                )
            }
        }
    }

    private fun JsonNode.getEdges(): ArrayNode {
        return get("result").get("data").get("allLandPressBlogPosts").get("edges") as ArrayNode
    }

    companion object {
        private val HTML_TAG_REGEX = """<[^>]*>""".toRegex()
    }
}
