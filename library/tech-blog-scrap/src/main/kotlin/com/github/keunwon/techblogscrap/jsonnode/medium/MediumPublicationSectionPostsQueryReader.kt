package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.select.Evaluator

class MediumPublicationSectionPostsQueryReader(
    private val mainUrl: String,
    override val url: String,
    override val query: String,
    override var variables: PublicationSectionPostsQuery,
    override val apiTemplate: ApiTemplate<JsonNode>,
    override val objectMapper: ObjectMapper,
) : MediumReader<PublicationSectionPostsQuery>() {
    private lateinit var postIds: List<String>
    private var endPage = 0

    override fun read(): BlogPost? {
        if (!initialized) {
            val document = Jsoup.parse(URL(mainUrl), 5000)
            val json = document.select(Evaluator.Tag("script"))
                .first { it.data().startsWith("window.__APOLLO_STATE__") }
                .data()
                .replace("window.__APOLLO_STATE__ = ", "")

            val jsonNodes = objectMapper.readTree(json)
            this.postIds = jsonNodes.fieldNames().let {
                val result = mutableListOf<String>()
                while (it.hasNext()) {
                    val key = it.next()
                    if (key.startsWith("Post:")) {
                        result.add(key.substring(5))
                    }
                }
                result
            }

            endPage = (postIds.size + pageSize - 1) / pageSize
            variables = PublicationSectionPostsQuery(postIds.slice(0 until pageSize))
        }

        return super.read()
    }

    override fun doNext(node: JsonNode) {
        val startIndex = pageSize * page
        val endIndex = (startIndex + pageSize).coerceAtMost(postIds.size)
        variables = PublicationSectionPostsQuery(postIds.slice(startIndex until endIndex))
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return page < endPage
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        return node.get("data").get("postResults").map { element ->
            element.run {
                BlogPost(
                    title = get("title").textValue(),
                    comment = get("extendedPreviewContent").get("subtitle").textValue(),
                    url = get("mediumUrl").textValue(),
                    authors = listOf(get("creator").get("name").textValue()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(get("firstPublishedAt").longValue()),
                )
            }
        }
    }
}

data class PublicationSectionPostsQuery(val postIds: List<String> = emptyList())
