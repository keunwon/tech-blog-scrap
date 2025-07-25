package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class SpoqaJsoupPagingReaderTest : FunSpec() {
    init {
        test("스포카 블로그 글 읽기") {
            val reader = SpoqaJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 140
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "Spoqa 기술 블로그를 오픈합니다.",
                comment = "스포카에서 기술 블로그를 오픈합니다.",
                url = "https://spoqa.github.io/2011/12/10/open.html",
                authors = listOf("김재석"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2011, 12, 10, 0, 0, 0),
            )
        }
    }
}
