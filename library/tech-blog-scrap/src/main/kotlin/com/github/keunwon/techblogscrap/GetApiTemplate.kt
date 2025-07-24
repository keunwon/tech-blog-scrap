package com.github.keunwon.techblogscrap

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GetApiTemplate(private val host: String) : ApiTemplate {
    override fun fetch(path: String, headers: Map<String, String>): Result<String> {
        return runCatching {
            val request = HttpRequest.newBuilder()
                .uri(URI("$host$path"))
                .apply {
                    headers.forEach { header(it.key, it.value) }
                }
                .build()

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { it.body() }
                .get()
        }
    }

    companion object {
        private val httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build()
    }
}
