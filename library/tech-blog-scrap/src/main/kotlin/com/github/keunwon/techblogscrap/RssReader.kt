package com.github.keunwon.techblogscrap

interface RssReader<T> {
    fun leastPosts(): List<T>
}
