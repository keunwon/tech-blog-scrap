package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class NaverJsonNodePagingReader(
    private val apiTemplate: GetApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var queryParams = "/api/v1/contents?categoryId=2&page=1&size=20"

    init {
        pageSize = 20
    }

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(queryParams)
    }

    override fun doNext(node: JsonNode) {
        queryParams = "/api/v1/contents?categoryId=2&page=$page&size=$pageSize"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        val totalPages = node.get("page").get("totalPages").intValue()
        val number = node.get("page").get("number").intValue()
        return totalPages > number
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val contents = node.get("content") as ArrayNode
        return contents.map { content ->
            BlogPost(
                title = content.get("postTitle").textValue(),
                comment = content.get("postHtml").textValue().take(50),
                url = "https://d2.naver.com${content.get("url")}",
                authors = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(content.get("postPublishedAt").longValue()),
            )
        }
    }
}
