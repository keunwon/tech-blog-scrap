package com.github.keunwon.techblogscrap

interface RssReader<T> {
    fun leastList(): List<T>
}
