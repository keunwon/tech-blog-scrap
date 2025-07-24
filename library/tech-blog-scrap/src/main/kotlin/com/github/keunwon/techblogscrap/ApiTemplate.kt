package com.github.keunwon.techblogscrap

interface ApiTemplate {
    fun fetch(data: String, headers: Map<String, String> = emptyMap()): Result<String>
}
