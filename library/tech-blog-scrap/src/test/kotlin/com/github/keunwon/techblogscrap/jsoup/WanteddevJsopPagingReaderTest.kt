package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class WanteddevJsopPagingReaderTest : FunSpec() {
    init {
        test("원티드 블로그 글 읽기") {
            val reader = WanteddevJsopPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBe 28
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.authors.shouldNotBeEmpty()
            }
            posts.last() shouldBe BlogPost(
                title = "Backend 서버 Down 원인 분석 및 문제해결하기",
                comment = "Wanted 서비스를 운영하면서 서버가 다운되는 현상을 간략하게 정리하고, 그 문제를 해결하는 과정을 정리해 보았습니다.",
                url = "https://wanteddev.github.io/server/2017/08/08/server-down-analysis.html",
                authors = listOf("홍진우"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2017, 8, 8, 0, 0, 0),
            )
        }
    }
}
