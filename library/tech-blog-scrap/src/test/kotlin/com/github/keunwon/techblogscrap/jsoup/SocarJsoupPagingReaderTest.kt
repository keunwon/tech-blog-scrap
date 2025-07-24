package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class SocarJsoupPagingReaderTest : FunSpec() {
    init {
        test("쏘카 기술블로그") {
            val reader = SocarJsoupPagingReader()

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThan 60
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.comment.shouldNotBeBlank()
                post.url.shouldNotBeBlank()
                post.authors.shouldNotBeEmpty()
                post.categories.shouldNotBeEmpty()
                post.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
