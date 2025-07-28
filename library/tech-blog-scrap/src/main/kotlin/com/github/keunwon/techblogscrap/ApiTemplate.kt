package com.github.keunwon.techblogscrap

interface ApiTemplate<T> {
    fun get(url: String, headers: Map<String, String> = emptyMap()): Result<T>

    fun post(url: String, data: Any, headers: Map<String, String> = emptyMap()): Result<T>

    fun postForm(url: String, form: Map<String, String>, headers: Map<String, String> = emptyMap()): Result<T>
}
