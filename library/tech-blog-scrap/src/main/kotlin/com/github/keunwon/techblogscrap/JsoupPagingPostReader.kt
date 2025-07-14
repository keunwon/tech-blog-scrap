package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import java.util.concurrent.CopyOnWriteArrayList

internal class JsoupPagingPostReader(
    private val pagingQueryProvider: PagingQueryProvider,
    private val postTemplate: PostTemplate,
    private val mapper: PagingMapper<List<Content>>,
) : AbstractPagingPostReader<Content>() {

    override fun doReadPage() {
        if (results.isEmpty()) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }

        val query = if (page == 0) {
            pagingQueryProvider.generateFirstQuery(page)
        } else {
            pagingQueryProvider.generateRemainingQuery(page)
        }

        runCatching {
            val item = mapper.map(postTemplate.fetch(query))
            if (page == 0) {
                pageSize = item.size
            }
            results.addAll(item)
        }.onFailure { ex ->
            when (ex) {
                is JsonMappingException, is JsonProcessingException -> {}
                else -> throw ex
            }
        }
    }
}
