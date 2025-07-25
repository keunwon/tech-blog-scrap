package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class OliveyoungJsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/page-data/index/page-data.json"

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(path)
    }

    override fun doNext(node: JsonNode) {
        val nextPage = node.get("result").get("pageContext").get("nextPagePath").textValue()
        path = "/page-data${nextPage}/page-data.json"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.get("result").get("pageContext").get("nextPagePath").textValue().isNotBlank()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val contents = node.get("result").get("data").get("allMarkdownRemark").get("nodes")
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = get("frontmatter").get("title").textValue(),
                    comment = get("frontmatter").get("subtitle").textValue(),
                    url = "https://oliveyoung.tech${get("fields").get("slug").textValue()}",
                    authors = emptyList(),
                    categories = get("frontmatter").get("tags").map { it.textValue() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH
                        .convert(get("frontmatter").get("date").textValue()),
                )
            }
        }
    }
}
