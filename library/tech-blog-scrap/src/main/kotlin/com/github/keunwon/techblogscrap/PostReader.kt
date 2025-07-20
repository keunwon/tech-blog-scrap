package com.github.keunwon.techblogscrap

interface PostReader<T> {
    fun read(): T?
}
