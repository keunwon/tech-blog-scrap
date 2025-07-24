package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class KurlyJsoupPagingReaderTest : FunSpec() {
    init {
        test("마켓컬리 블로그 글 읽기") {
            val reader = KurlyJsoupPagingReader()

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 100
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.url.shouldNotBeBlank()
                post.authors.shouldNotBeEmpty()
                post.publishedDateTime.shouldNotBeNull()
            }
            posts.forSome { it.comment.shouldNotBeBlank() }
        }
    }
}
