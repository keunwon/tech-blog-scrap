package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class TossJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var path = "/api-public/v3/ipd-thor/api/v1/workspaces/15/posts?size=20&page=1"

    init {
        pageSize = 20
    }

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://api-public.toss.im$path")
    }

    override fun doNext(node: JsonNode) {
        path = node.get("success").get("next").textValue()
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return !node.get("success").get("next").isNull
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("success").get("results").map { element ->
            element.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("subtitle").textValue(),
                    url = "https://toss.tech/article/${get("seoConfig").get("urlSlug").textValue()}",
                    authors = get("editor").get("name").textValue().split("/"),
                    categories = get("seoConfig").get("tags").map { it.get("content").textValue() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON
                        .convert(get("publishedTime").textValue().take(19)),
                )
            }
        }
    }
}
