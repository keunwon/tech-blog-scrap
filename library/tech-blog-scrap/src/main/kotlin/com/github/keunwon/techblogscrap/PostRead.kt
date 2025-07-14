package com.github.keunwon.techblogscrap

internal interface PostRead<T> {
    fun readOrNull(): T?
}
