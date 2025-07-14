package com.github.keunwon.techblogscrap

internal interface PagingTemplate {
    fun fetch(query: String): String
}
