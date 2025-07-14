package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import java.util.concurrent.CopyOnWriteArrayList

internal class JsonPagingPostReader(
    private val pagingQueryProvider: PagingQueryProvider,
    private val pagingTemplate: PagingTemplate,
    private val mapper: PagingMapper<BlogPost>,
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
            val item = mapper.map(pagingTemplate.fetch(query))
            if (page == 0) {
                pageSize = item.page.pageSize
                current = item.page.current
            }
            results.addAll(item.contents)
        }.onFailure { ex ->
            when (ex) {
                is JsonMappingException, is JsonProcessingException -> {}
                else -> throw ex
            }
        }
    }
}
