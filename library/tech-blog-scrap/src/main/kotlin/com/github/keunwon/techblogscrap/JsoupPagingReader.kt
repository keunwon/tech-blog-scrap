package com.github.keunwon.techblogscrap

import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class JsoupPagingReader<T> : AbstractPagingReader<T>() {
    private lateinit var document: Document

    override fun doReadPage() {
        document = Jsoup.parse(URL(getRequestUrl()), 5000)
        results = convert(document)
        ++page
        current = 0

        if (pageSize != results.size) {
            logger.info("default pageSize $pageSize, result size ${results.size} page size 변경합니다.")
            pageSize = results.size
        }
    }

    override fun next() {
        doNext(document)
    }

    override fun hasNextPage(): Boolean {
        return doHasNetPage(document)
    }

    open fun doNext(document: Document) {}

    abstract fun getRequestUrl(): String

    abstract fun convert(document: Document): List<T>

    abstract fun doHasNetPage(document: Document): Boolean
}
