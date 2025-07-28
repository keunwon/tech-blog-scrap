package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class NHNCloudJsonNodePagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private var pageNo = 1

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://meetup.nhncloud.com/tcblog/v1.0/posts?pageNo=${pageNo}&rowPerPage=12")
    }

    override fun doNext(node: JsonNode) {
        ++pageNo
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return !node.get("posts").isEmpty
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("posts").map { post ->
            post.run {
                BlogPost(
                    title = get("postPerLang").get("title").textValue(),
                    comment = get("contentPreview").textValue(),
                    url = "https://meetup.nhncloud.com/posts/${get("postId").intValue()}",
                    authors = emptyList(),
                    categories = get("postPerLang").get("tag").textValue().replace("#", "").split(" "),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON
                        .convert(get("regTime").textValue().take(19)),
                )
            }
        }
    }
}
