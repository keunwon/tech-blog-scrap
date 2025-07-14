package com.github.keunwon.techblogscrap

internal class SimplePagingQueryProvider(
    private val initUrl: String,
    private val urlTemplate: String,
) : PagingQueryProvider {
    override fun generateFirstQuery(page: Int): String = initUrl

    override fun generateRemainingQuery(page: Int): String = urlTemplate.format(page)
}
