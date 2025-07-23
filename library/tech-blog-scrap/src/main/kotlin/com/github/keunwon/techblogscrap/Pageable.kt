package com.github.keunwon.techblogscrap

interface Pageable {
    fun next()

    fun hasNextPage(): Boolean
}
