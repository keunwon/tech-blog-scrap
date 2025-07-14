package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import java.util.concurrent.CopyOnWriteArrayList

internal class JsoupAndJsonPagingPostReader(
    private val pagingQueryProvider: PagingQueryProvider,
    private val jsoupPostTemplate: PostTemplate,
    private val jsonPostTemplate: PostTemplate,
    private val jsoupMapper: PagingMapper<BlogPost>,
    private val jsonMapper: PagingMapper<BlogPost>,
) : AbstractPagingPostReader<Content>() {

    override fun doReadPage() {
        if (results.isEmpty()) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }

        runCatching {
            val data = if (page == 0) {
                val query = pagingQueryProvider.generateFirstQuery(page)
                jsoupMapper.map(jsoupPostTemplate.fetch(query))
            } else {
                val query = pagingQueryProvider.generateRemainingQuery(page)
                jsonMapper.map(jsonPostTemplate.fetch(query))
            }
            results.addAll(data.contents)
        }.onFailure { ex ->
            when (ex) {
                is JsonMappingException, is JsonProcessingException -> {}
                else -> throw ex
            }
        }
    }
}
