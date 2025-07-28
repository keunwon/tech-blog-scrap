package com.github.keunwon.techblogscrap.jsonnode

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsonNodePagingReader

class SkPlanetJsonPagingReader(
    private val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : JsonNodePagingReader<BlogPost>() {
    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.get("https://techtopic.skplanet.com/page-data/index/page-data.json")
    }

    override fun doNext(node: JsonNode) {
        return
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return false
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("result").get("data").get("allMarkdownRemark").get("nodes").map { element ->
            element.run {
                BlogPost(
                    title = get("frontmatter").get("title").textValue(),
                    comment = get("excerpt").textValue(),
                    url = "https://techtopic.skplanet.com/skp-techblog-intro/",
                    authors = listOf(get("frontmatter").get("author").textValue()),
                    categories = get("frontmatter").get("tags").map { it.textValue() },
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY
                        .convert(get("frontmatter").get("date").textValue()),
                )
            }
        }
    }
}
