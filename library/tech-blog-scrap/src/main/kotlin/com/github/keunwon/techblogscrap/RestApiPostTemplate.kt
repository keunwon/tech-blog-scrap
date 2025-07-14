package com.github.keunwon.techblogscrap

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

internal class RestApiPostTemplate : PostTemplate {
    private val httpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    override fun fetch(query: String): String {
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(query))
            .build()

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply { it.body() }
            .get()
    }
}
