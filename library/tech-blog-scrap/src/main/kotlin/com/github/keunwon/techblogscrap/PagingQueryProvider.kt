package com.github.keunwon.techblogscrap

internal interface PagingQueryProvider {
    fun generateFirstQuery(page: Int): String

    fun generateRemainingQuery(page: Int): String
}
