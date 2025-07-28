package com.github.keunwon.techblogscrap.jsonnode.medium

import com.fasterxml.jackson.databind.JsonNode
import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.JsonNodePagingReader

abstract class MediumReader<T> : JsonNodePagingReader<BlogPost>() {
    abstract val url: String
    abstract val query: String
    abstract var variables: T
    abstract val apiTemplate: ApiTemplate<JsonNode>

    override fun fetchResponse(): Result<JsonNode> {
        return apiTemplate.post(
            url = url,
            data = mapOf(
                "query" to query,
                "variables" to variables,
            ),
        )
    }
}
