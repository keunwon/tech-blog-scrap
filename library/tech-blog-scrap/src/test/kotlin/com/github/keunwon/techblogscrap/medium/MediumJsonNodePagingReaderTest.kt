package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class MediumJsonNodePagingReaderTest : FunSpec() {
    init {
        test("무신사 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReader("musinsa-tech")

            val posts = generateSequence { reader.read() }.toList()

            shouldAll(posts, 34)
        }

        test("왓챠 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReader("watcha")

            val posts = generateSequence { reader.read() }.toList()

            shouldAll(posts, 29)
        }
    }

    private val apiTemplate =
        RestApiTemplate("https://medium.com/_/graphql", HttpMethod.POST)
    private val queryPath =
        MediumJsonNodePagingReaderTest::class.java.classLoader.getResource("query/medium.txt")!!.file

    private fun generateMediumJsonNodePagingReader(username: String) =
        MediumJsonNodePagingReader(
            username = username,
            queryPath = queryPath,
            apiTemplate = apiTemplate,
            objectMapper = testObjectMapper,
        )

    private fun shouldAll(posts: List<BlogPost>, minSize: Int) {
        posts.size shouldBeGreaterThanOrEqual minSize
        posts.forAll { post ->
            post.title.shouldNotBeBlank()
            post.publishedDateTime.shouldNotBeNull()
        }
    }
}
