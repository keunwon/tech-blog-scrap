package com.github.keunwon.techblogscrap.other

import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class LineJsonNodePagingReaderTest : FunSpec() {
    init {
        test("라인 블로그 글 읽기") {
            val reader = LineJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://techblog.lycorp.co.jp"),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 90
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.url.shouldNotBeBlank()
                post.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
