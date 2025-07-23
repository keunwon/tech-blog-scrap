package com.github.keunwon.techblogscrap.medium

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MediumApiPagingReaderTest : FunSpec() {
    private val query by lazy {
        val resource = MediumApiPagingReaderTest::class.java.classLoader.getResource("query/medium.txt")!!
        BufferedReader(InputStreamReader(File(resource.file).inputStream())).use { br -> br.readText() }
    }

    private val objectMapper = ObjectMapper()

    private val apiTemplate = RestApiTemplate("https://medium.com/_/graphql", HttpMethod.POST)

    init {
        test("왓챠 블로그 글 읽기") {
            val reader = MediumApiPagingReader(
                username = "watcha",
                query = query,
                apiTemplate = apiTemplate,
                objectMapper = objectMapper,
            )

            val blogPosts = generateSequence { reader.read() }.toList()

            shouldAll(blogPosts)
        }

        test("무신사 블로그 글 읽기") {
            val reader = MediumApiPagingReader(
                username = "musinsa-tech",
                query = query,
                apiTemplate = apiTemplate,
                objectMapper = objectMapper,
            )

            val blogPosts = generateSequence { reader.read() }.toList()

            shouldAll(blogPosts)
        }
    }

    private fun shouldAll(blogPosts: List<BlogPost>) {
        blogPosts.size shouldBeGreaterThan 10
        blogPosts.forEach { blogPost ->
            blogPost.title.shouldNotBeBlank()
            blogPost.publishedDateTime.shouldNotBeNull()
        }
    }
}
