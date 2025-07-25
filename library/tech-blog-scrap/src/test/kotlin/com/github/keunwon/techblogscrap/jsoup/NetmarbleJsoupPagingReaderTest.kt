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

class NetmarbleJsoupPagingReaderTest : FunSpec() {
    init {
        test("넷마블 블로그 글 읽기") {
            val reader = NetmarbleJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 64
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "가볍게 쓰려했던 WSL2가 무겁게 다가온 순간",
                comment = "저는 매우 라이트한 사용자입니다. 개발 관련 업무를 직접적으로 하지 않기 때문에, 간단한 웹 페이지 수정을 확인하는 정도 또는 매뉴얼에 작성된 코드를 그대로 재현하면서 따라해보는 정도가 대부분입니다. 그래서 WSL에 우분투를 올려서 쓸 때 개인적으로 생각한 장점은 크게 2가지가…",
                url = "https://netmarble.engineering/journey-to-wsl2-and-trouble-shooting/",
                authors = listOf("조병승"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2021, 9, 3, 0, 0, 0),
            )
        }
    }
}
