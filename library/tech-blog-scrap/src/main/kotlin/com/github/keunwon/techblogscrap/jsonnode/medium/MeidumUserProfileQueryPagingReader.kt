package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.LongNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions

class MeidumUserProfileQueryPagingReader(
    override val queryPath: String,
    override var variables: UserProfileQuery,
    override val apiTemplate: ApiTemplate,
    override val objectMapper: ObjectMapper,
) : MediumReader<UserProfileQuery>() {

    init {
        pageSize = variables.homepagePostsLimit
    }

    override fun doNext(node: JsonNode) {
        val homepagePostsFrom = node.getPagingInfo().get("next").get("from")
        require(homepagePostsFrom != null) { "homepagePostsFrom is null" }
        variables = variables.copy(homepagePostsFrom = homepagePostsFrom.textValue())
    }

    override fun doHasNextPage(node: JsonNode): Boolean {
        return node.getPagingInfo().get("next").get("from") != null
    }

    override fun convert(node: JsonNode): List<BlogPost> {
        val posts = node.get("data").get("userResult").get("homepagePostsConnection").get("posts") as ArrayNode
        return posts.map { post ->
            BlogPost(
                title = post.get("title").textValue(),
                comment = post.get("extendedPreviewContent").get("subtitle").textValue(),
                url = post.get("mediumUrl").textValue(),
                authors = post.get("creator").get("username").textValue().split(" "),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert((post.get("firstPublishedAt") as LongNode).longValue()),
            )
        }
    }

    private fun JsonNode.getPagingInfo(): JsonNode {
        return get("data").get("userResult").get("homepagePostsConnection").get("pagingInfo")
    }
}

data class UserProfileQuery(
    val homepagePostsFrom: String?,
    val includeDistributedResponses: Boolean,
    val includeShouldFollowPostForExternalSearch: Boolean,
    val id: String?,
    val username: String?,
    val homepagePostsLimit: Int,
) {
    companion object {
        fun ofUserName(username: String) = UserProfileQuery(
            homepagePostsFrom = null,
            includeDistributedResponses = true,
            includeShouldFollowPostForExternalSearch = true,
            id = null,
            username = username,
            homepagePostsLimit = 20,
        )

        fun ofId(id: String) = UserProfileQuery(
            homepagePostsFrom = null,
            includeDistributedResponses = true,
            includeShouldFollowPostForExternalSearch = true,
            id = id,
            username = null,
            homepagePostsLimit = 20,
        )
    }
}
