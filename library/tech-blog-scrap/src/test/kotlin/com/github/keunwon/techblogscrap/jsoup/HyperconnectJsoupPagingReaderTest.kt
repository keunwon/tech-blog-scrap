package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class HyperconnectJsoupPagingReaderTest : FunSpec() {
    init {
        test("하이퍼커넥스 블로그 글 일기") {
            val reader = HyperconnectJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 106
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
