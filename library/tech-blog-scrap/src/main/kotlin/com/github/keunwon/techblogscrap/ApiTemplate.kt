package com.github.keunwon.techblogscrap

interface ApiTemplate {
    fun fetch(json: String): Result<String>
}
