package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

abstract class JsonNodePagingReader<T> : AbstractPagingReader<T>() {
    private lateinit var currentNode: JsonNode
    abstract val objectMapper: ObjectMapper

    override fun doReadPage() {
        val response = fetchResponse()
            .onFailure { e -> logger.warn("api 요청에 실패하였습니다.", e) }
            .getOrNull()
            ?: return

        currentNode = objectMapper.readTree(response)
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

    abstract fun fetchResponse(): Result<String>

    abstract fun doNext(node: JsonNode)

    abstract fun doHasNextPage(node: JsonNode): Boolean

    abstract fun convert(node: JsonNode): List<T>
}
