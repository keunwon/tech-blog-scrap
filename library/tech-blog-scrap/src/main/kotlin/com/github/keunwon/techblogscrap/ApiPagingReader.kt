package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory

abstract class ApiPagingReader<T> : PostReader<T>, Pageable {
    protected val logger = LoggerFactory.getLogger(ApiPagingReader::class.java)

    protected abstract val objectMapper: ObjectMapper
    protected abstract val apiTemplate: ApiTemplate

    protected lateinit var currentNode: JsonNode
    protected var initialized = false
    protected var pageSize = 10
    private var page = 0
    private var current = 0
    private var results = listOf<T>()

    @Suppress("UNCHECKED_CAST")
    override fun read(): T? {
        if (!initialized || results.isEmpty() || current >= pageSize) {
            if (initialized) {
                if (!hasNextPage()) return null
                next()
            }

            logger.info("reading ${page++}page...")

            val responseJson = apiTemplate.fetch(objectMapper.writeValueAsString(getRequestBody()))
                .onFailure { e -> logger.warn("api 요청에 실패하였습니다.", e) }
                .getOrNull() ?: return null

            currentNode = objectMapper.readTree(responseJson)
            results = convert(currentNode)
            current = 0
            pageSize = results.size

            if (!initialized) initialized = true
        }

        val next = current++
        return if (next < results.size) results[next] else null
    }

    protected abstract fun convert(jsonNode: JsonNode): List<T>

    protected abstract fun getRequestBody(): Any
}
