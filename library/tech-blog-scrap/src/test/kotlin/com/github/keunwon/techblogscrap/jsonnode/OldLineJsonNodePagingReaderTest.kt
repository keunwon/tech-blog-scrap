package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class OldLineJsonNodePagingReaderTest : FunSpec() {
    init {
        test("구 라인 블로그 글 읽기") {
            val reader = OldLineJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://engineering.linecorp.com"),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 100
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.comment.shouldNotBeBlank()
                post.authors.shouldNotBeEmpty()
                post.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
