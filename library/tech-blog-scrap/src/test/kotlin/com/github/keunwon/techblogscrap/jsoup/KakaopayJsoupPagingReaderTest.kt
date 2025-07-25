package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class KakaopayJsoupPagingReaderTest : FunSpec() {
    init {
        test("카카오페이 기술블로그") {
            val reader = KakaopayJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 142
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
