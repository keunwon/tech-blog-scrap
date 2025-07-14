package com.github.keunwon.techblogscrap

import com.github.keunwon.techblogscrap.support.BlogTagYamlParser
import com.github.keunwon.techblogscrap.support.objectMapper
import java.io.File

class PagingPostReaderApplication {
    private val resource = PagingPostReaderApplication::class.java.classLoader.getResource("blog.yml")!!
    private val handler = PagingPostReaderHandler()

    fun start() {
        val blogTagProperty = BlogTagYamlParser.load(File(resource.path))
        val pagingTemplate = HttpClientPagingTemplate()

        val jsonPagingPostReaders = blogTagProperty.json.map { property ->
            val pagingQueryProvider = SimplePagingQueryProvider(property.firstUrl, property.remainingUrlTemplate)
            val mapper = JsonPagingMapper(property, objectMapper)
            JsonPagingPostReader(pagingQueryProvider, pagingTemplate, mapper)
        }

        val jsoupPagingPostReaders = blogTagProperty.jsoup.map { property ->
            val pagingQueryProvider = SimplePagingQueryProvider(property.firstUrl, property.remainingUrlTemplate)
            val mapper = JsoupPagingMapper(property)
            JsoupPagingPostReader(pagingQueryProvider, pagingTemplate, mapper)
        }

        jsonPagingPostReaders.forEach { handler.handle(it) }
        jsoupPagingPostReaders.forEach { handler.handle(it) }

        handler.shutdown()
    }
}
