package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions

class MediumPublicationContentDataQueryReader(
    override val queryPath: String,
    override var variables: PublicationContentDataQuery,
    override val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : MediumReader<PublicationContentDataQuery>() {

    override fun doNext(node: JsonNode) {
        val endCursor = node.getPagingInfo().get("endCursor").textValue()
        variables = variables.copy(after = endCursor)
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPagingInfo().get("hasNextPage").booleanValue()
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val contents = node.get("data").get("publication").get("publicationPostsConnection").get("edges")
        return contents.map { content ->
            content.get("node").run {
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

    private fun JsonNode.getPagingInfo(): JsonNode {
        return get("data").get("publication").get("publicationPostsConnection").get("pageInfo")
    }
}

data class PublicationContentDataQuery(
    val ref: Ref,
    val first: Int,
    val after: String,
    val orderBy: OrderBy,
    val filter: Map<String, Any>,
) {
    data class Ref(val slug: String?, val domain: String?)

    data class OrderBy(val publishedAt: String = "DESC")

    companion object {
        fun ofSlug(
            slug: String,
            filter: Map<String, Any> = mapOf("published" to true),
        ) = PublicationContentDataQuery(
            ref = Ref(slug, null),
            first = 10,
            after = "",
            orderBy = OrderBy("DESC"),
            filter = filter,
        )

        fun ofDomain(
            domain: String,
            filter: Map<String, Any> = mapOf("published" to true),
        ) = PublicationContentDataQuery(
            ref = Ref(null, domain),
            first = 10,
            after = "",
            orderBy = OrderBy("DESC"),
            filter = mapOf("published" to true),
        )
    }
}
