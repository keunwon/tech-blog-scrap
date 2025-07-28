package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiJsonNodeTemplate(val client: OkHttpClient, val mapper: ObjectMapper) : ApiTemplate<JsonNode> {
    override fun get(
        url: String,
        headers: Map<String, String>,
    ): Result<JsonNode> =
        runCatching {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = client.newCall(request).execute()

            mapper.readTree(response.body.bytes())
        }

    override fun post(
        url: String,
        data: Any,
        headers: Map<String, String>,
    ): Result<JsonNode> = runCatching {
        val request = Request.Builder()
            .url(url)
            .post((data as? String ?: mapper.writeValueAsString(data)).toRequestBody("application/json".toMediaType()))
            .build()
        val response = client.newCall(request).execute()

        mapper.readTree(response.body.bytes())
    }

    override fun postForm(
        url: String,
        form: Map<String, String>,
        headers: Map<String, String>,
    ): Result<JsonNode> = runCatching {
        val formData = FormBody.Builder()
            .apply { form.forEach { add(it.key, it.value) } }
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formData)
            .build()
        val response = client.newCall(request).execute()

        mapper.readTree(response.body.bytes())
    }
}
