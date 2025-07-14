package com.github.keunwon.techblogscrap

internal interface PagingMapper<T> {
    fun map(data: String): T
}
