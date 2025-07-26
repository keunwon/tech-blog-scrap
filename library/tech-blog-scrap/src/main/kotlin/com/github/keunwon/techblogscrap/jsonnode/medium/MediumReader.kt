package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.ApiTemplate
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.JsonNodePagingReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

abstract class MediumReader<T> : JsonNodePagingReader<BlogPost>() {
    abstract val queryPath: String
    abstract var variables: T
    abstract val apiTemplate: ApiTemplate

    protected val query: String by lazy {
        BufferedReader(InputStreamReader(File(queryPath).inputStream())).use { it.readText() }
    }

    override fun fetchResponse(): Result<String> {
        return apiTemplate.fetch(
            data = objectMapper.writeValueAsString(
                mapOf(
                    "query" to query,
                    "variables" to variables,
                )
            ),
            headers = mapOf("Content-type" to "application/json"),
        )
    }
}
