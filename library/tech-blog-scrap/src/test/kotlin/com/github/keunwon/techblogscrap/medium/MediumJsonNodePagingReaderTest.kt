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
        val apiTemplate =
            RestApiTemplate("https://medium.com/_/graphql", HttpMethod.POST)
        val queryPath = MediumJsonNodePagingReaderTest::class.java.classLoader.getResource("query/medium.txt")!!.file

        test("무신사 블로그 글 읽기") {
            val reader = MediumJsonNodePagingReader(
                username = "musinsa-tech",
                queryPath = queryPath,
                apiTemplate = apiTemplate,
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            shouldAll(posts, 34)
        }

        test("왓챠 블로그 글 읽기") {
            val reader = MediumJsonNodePagingReader(
                username = "watcha",
                queryPath = queryPath,
                apiTemplate = apiTemplate,
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            shouldAll(posts, 29)
        }
    }

    private fun shouldAll(posts: List<BlogPost>, minSize: Int) {
        posts.size shouldBeGreaterThanOrEqual minSize
        posts.forAll { post ->
            post.title.shouldNotBeBlank()
            post.publishedDateTime.shouldNotBeNull()
        }
    }
}
