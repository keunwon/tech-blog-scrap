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

class SaraminJsoupPagingReaderTest : FunSpec() {
    init {
        test("사람인 블로그 글 읽기") {
            val reader = SaraminJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 57
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "Hello World",
                comment = "Hello World, Hello Saramin Tech Blog",
                url = "https://saramin.github.io/2017-06-02-hello-world-saramin-tech-blog/",
                authors = listOf("남광현"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2017, 6, 2, 0, 0, 0),
            )
        }
    }
}
