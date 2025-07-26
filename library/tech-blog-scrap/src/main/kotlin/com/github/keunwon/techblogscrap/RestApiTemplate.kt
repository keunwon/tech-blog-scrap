package com.github.keunwon.techblogscrap

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RestApiTemplate(
    private val url: String,
    private val httpMethod: HttpMethod,
) : ApiTemplate {
    override fun fetch(data: String, headers: Map<String, String>): Result<String> {
        return runCatching {
            val request = HttpRequest.newBuilder()
                .uri(URI(url))
                .apply {
                    when (httpMethod) {
                        HttpMethod.GET -> GET()
                        HttpMethod.POST -> POST(HttpRequest.BodyPublishers.ofString(data))
                    }
                    headers.forEach { (k, v) -> header(k, v) }
                    header(
                        "User-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36"
                    )
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
