package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class AB180JsonNodePagingReader(
    private val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    private val requestJson = """
        {"collectionView":{"id":"c56869fe-baa8-4f75-a7ac-701cc3bfedf0","spaceId":"56af06bd-af23-41ed-9eae-5faeee5a75ac"},"loader":{"type":"reducer","searchQuery":"","reducers":{"collection_group_results":{"type":"results","limit":35}},"userTimeZone":"Asia/Seoul","filter":{"operator":"and","filters":[{"operator":"and","filters":[{"filter":{"value":{"type":"exact","value":"Released"},"operator":"enum_is"},"property":"cpEm"}]}]},"sort":[{"property":"^\\mI","direction":"descending"},{"property":"title","direction":"ascending"}]},"source":{"id":"ce6b021b-1910-4fad-a7ab-d10c76e957a1","type":"collection","spaceId":"56af06bd-af23-41ed-9eae-5faeee5a75ac"}}
    """.trimIndent()

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(requestJson, mapOf("Content-type" to "application/json"))
    }

    override fun doNext(node: JsonNode) {
        return
    }

    override fun doHasNextPage(node: JsonNode): Boolean = false

    override fun convert(node: JsonNode): List<BlogPost> {
        val blocks = node.get("recordMap").get("block")
        return node.get("allBlockIds").map { jn ->
            val id = jn.textValue()
            blocks.get(id).get("value").run {
                BlogPost(
                    title = get("properties").get("title").first().first().textValue(),
                    comment = "",
                    url = "https://engineering.ab180.co/$id",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("created_time").longValue()),
                )
            }
        }
    }
}
