package com.github.keunwon.techblogscrap

internal interface PostTemplate {
    fun fetch(query: String): String
}
