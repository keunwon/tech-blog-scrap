package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class KakaostyleJsoupPagingReaderTest : FunSpec() {
    init {
        test("카카오 스타일 블로그글 읽기") {
            val reader = KakaostyleJsoupPagingReader()

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 30
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.authors.shouldNotBeEmpty()
                post.publishedDateTime.shouldNotBeNull()
            }.forSome { post ->
                post.comment.shouldNotBeBlank()
            }
        }
    }
}
