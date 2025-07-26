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

class SquarelabJsoupPagingReaderTest : FunSpec() {
    init {
        test("스퀘어랩 블로그 글 읽기") {
            val reader = SquarelabJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 30
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "Serverless한 회사 내부 서비스 만들기",
                comment = "사내에서 사용되는 서비스의 경우 대중을 대상으로 하지 않기 때문에 사용량이 많지 않은 경우가 대부분입니다. AWS Lambda를 사용하면 비용 절감뿐만 아니라 서버를 직접 관리하고 운영할...",
                url = "https://squarelab.co/blog/developing-internal-service-with-serverless-aws/",
                authors = listOf("권영재"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2020, 2, 10, 0, 0, 0),
            )
        }
    }
}
