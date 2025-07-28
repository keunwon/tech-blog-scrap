package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

abstract class JsonNodePagingReader<T> : AbstractPagingReader<T>() {
    abstract val objectMapper: ObjectMapper

    private lateinit var currentNode: JsonNode

    override fun doReadPage() {
        currentNode = fetchResponse()
            .onFailure { e -> logger.warn("api 요청에 실패하였습니다.", e) }
            .getOrNull()
            ?: return
        results = convert(currentNode)
        ++page
        current = 0

        if (pageSize != results.size) {
            logger.info("default pageSize $pageSize, result size ${results.size} page size 변경합니다.")
            pageSize = results.size
        }
    }

    override fun next() {
        doNext(currentNode)
    }

    override fun hasNextPage(): Boolean = doHasNextPage(currentNode)

    abstract fun fetchResponse(): Result<JsonNode>

    abstract fun doNext(node: JsonNode)

    abstract fun doHasNextPage(node: JsonNode): Boolean

    abstract fun convert(node: JsonNode): List<T>
}
