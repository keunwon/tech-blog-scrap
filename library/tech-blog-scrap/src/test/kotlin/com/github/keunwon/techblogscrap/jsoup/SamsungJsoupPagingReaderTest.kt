package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class SamsungJsoupPagingReaderTest : FunSpec() {
    init {
        test("삼성 블로그 글 읽기") {
            val reader = SamsungJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 65
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "메모리 유효성 체크 작업을 자동화할 수 있는 Rust 라이브러리",
                comment = "",
                url = "https://techblog.samsung.com/blog/article/2",
                authors = listOf("Igor Kotrasinski 외 1명"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2023, 8, 31, 0, 0, 0),
            )
        }
    }
}
