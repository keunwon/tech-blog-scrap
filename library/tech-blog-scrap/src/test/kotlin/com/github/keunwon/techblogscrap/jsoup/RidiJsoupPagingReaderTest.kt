package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.string.shouldBeBlank

class RidiJsoupPagingReaderTest : FunSpec() {
    init {
        test("Ridi 블로그 글 읽기") {
            val reader = RidiJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 79
            posts.forAll {
                it.title.shouldBeBlank()
                it.comment.shouldBeBlank()
                it.url.shouldBeBlank()
            }
        }
    }
}
