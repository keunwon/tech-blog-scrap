package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class KakaoJsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val params: MutableMap<String, Any?> = mutableMapOf(
        "categoryCode" to "blog",
        "lastSeq" to 0,
        "firstSeq" to 0,
        "lastPageNumber" to 0,
        "firstPageNumber" to 0,
    )

    override fun fetchResponse(): Result<String> {
        val urlParams = params.mapNotNull { if (it.value != null) "${it.key}=${it.value}" else null }.joinToString("&")
        return apiTemplate.fetch("?$urlParams")
    }

    override fun doNext(node: JsonNode) {
        params["lastSeq"] = node.get("lastSeq").intValue()
        params["firstSeq"] = node.get("firstSeq").intValue()
        params["lastPageNumber"] = node.get("lastPageNumber").intValue()
        params["firstPageNumber"] = node.get("firstPageNumber").intValue()
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return (node.get("nextBlock") as BooleanNode).booleanValue()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val arrayNodes = node.get("pages") as ArrayNode
        return arrayNodes.flatMap { pageNode ->
            pageNode.get("contents").map { content ->
                content.run {
                    BlogPost(
                        title = get("title").textValue(),
                        comment = "",
                        url = "https://tech.kakao.com/posts/${get("id").intValue()}",
                        authors = get("authors").map { it.get("name").textValue() },
                        categories = emptyList(),
                        publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA_T_HH_MM_SS_COLON
                            .convert(get("releaseDateTime").textValue().replace(" ", "T"))
                    )
                }
            }
        }
    }
}
