package com.github.keunwon.techblogscrap.other

import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class NaverJsonNodePagingReaderTest : FunSpec() {
    init {
        test("네이버 블로그 글 읽기") {
            val reader = NaverJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://d2.naver.com"),
                objectMapper = testObjectMapper,
            )

            val blogPosts = generateSequence { reader.read() }.toList()

            blogPosts.size shouldBeGreaterThan 10
            blogPosts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.url.shouldNotBeBlank()
                post.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
