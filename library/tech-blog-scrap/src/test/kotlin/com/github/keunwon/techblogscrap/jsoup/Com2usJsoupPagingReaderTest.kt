package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class Com2usJsoupPagingReaderTest : FunSpec() {
    init {
        test("컴투스 블로그 글 읽기") {
            val readers = Com2usJsoupPagingReader()
            val posts = generateSequence { readers.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 41
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.categories.shouldNotBeEmpty()
            }
            posts.last() shouldBe BlogPost(
                title = "Google 클라우드 플랫폼 기반 빅데이터 시스템 구축기",
                comment = "",
                url = "https://on.com2us.com/tech/google-클라우드-플랫폼-기반-빅데이터-시스템-구축기/",
                authors = emptyList(),
                categories = listOf("기술이야기", "하이브", "애널리틱스", "기술블로그", "하이브"),
                publishedDateTime = null,
            )
        }
    }
}
