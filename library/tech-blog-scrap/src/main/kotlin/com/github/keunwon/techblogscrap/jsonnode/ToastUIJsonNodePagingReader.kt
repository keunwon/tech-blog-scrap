package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class ToastUIJsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/page-data/posts/ko/page-data.json"

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(path)
    }

    override fun doNext(node: JsonNode) {
        path = "/page-data/posts/ko/${page + 1}/page-data.json"
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return !node.get("result").get("pageContext").get("next").isNull
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("result").get("data").get("allPosts").get("edges").map { jsonNode ->
            jsonNode.get("node").get("frontmatter").run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("description").textValue(),
                    url = "https://ui.toast.com/posts/${get("id").textValue()}",
                    authors = get("author").textValue().split(","),
                    categories = get("tags").map { it.textValue() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA.convert(get("date")!!.textValue()),
                )
            }
        }
    }
}
