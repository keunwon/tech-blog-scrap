package com.github.keunwon.techblogscrap

import org.slf4j.LoggerFactory

/**
 * JsonNode, Jsoup 등 다양한 스크래핑 방식 지원을 위해서 추상 클래스로 분리
 *
 * @param T
 */
abstract class AbstractPagingReader<T> : PostReader<T>, Pageable {
    protected val logger = LoggerFactory.getLogger(AbstractPagingReader::class.java)

    protected var initialized = false
    protected var pageSize = 10
    protected var page = 0
    protected var current = 0
    protected var results = listOf<T>()

    override fun read(): T? {
        if (!initialized) {
            logger.info("first page reading...")

            doReadPage()
            initialized = true
        } else if (current >= pageSize) {
            if (!hasNextPage()) return null

            logger.info("reading ${page}page...")
            next()
            doReadPage()
        }

        val next = current++
        return if (next < results.size) results[next] else null
    }

    abstract fun doReadPage()
}
