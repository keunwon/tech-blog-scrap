package com.github.keunwon.techblogscrap

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RestApiTemplate(
    private val url: String,
    private val httpMethod: HttpMethod,
    private val headers: Map<String, String> = mapOf(),
) : ApiTemplate {
    override fun fetch(json: String): Result<String> {
        return runCatching {
            val request = HttpRequest.newBuilder()
                .uri(URI(url))
                .apply {
                    when (httpMethod) {
                        HttpMethod.GET -> GET()
                        HttpMethod.POST -> POST(HttpRequest.BodyPublishers.ofString(json))
                    }
                    headers.forEach { (k, v) -> header(k, v) }
                }
                .build()

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { resolveJson(it.body()) }
                .get()
        }
    }

    private fun resolveJson(json: String): String {
        return json.replace("""])}while(1);</x>""", "")
    }

    companion object {
        private val httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build()
    }
}

enum class HttpMethod {
    GET, POST;
}
