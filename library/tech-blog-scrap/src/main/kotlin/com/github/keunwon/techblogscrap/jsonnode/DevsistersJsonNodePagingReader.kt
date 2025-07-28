package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class DevsistersJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://tech.devsisters.com/page-data/index/page-data.json?page=1")
    }

    override fun doNext(node: JsonNode) {
        throw UnsupportedOperationException("")
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return false
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val contents = node.get("result").get("data").get("allMarkdownRemark").get("nodes")
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = get("frontmatter").get("title").textValue(),
                    comment = get("frontmatter").get("summary").textValue(),
                    url = "https://tech.devsisters.com${get("fields").get("path").textValue()}",
                    authors = get("fields").get("authors").map { it.get("displayName").textValue() },
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH.convert(get("fields").get("date").textValue()),
                )
            }
        }
    }
}
