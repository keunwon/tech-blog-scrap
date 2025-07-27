package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class SKCCJsoupPagingReaderTest : FunSpec() {
    init {
        test("SK C&C 블로그 글 읽기") {
            val reader = SKCCJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 245
            posts.last() shouldBe BlogPost(
                title = "Exception 가이드",
                comment = "Spring에서 어떤 방식으로 Exception을 처리하는지 이해하고, 적용하는 방법을 간단한 예제 프로젝트를 만들었습니다.",
                url = "https://engineering-skcc.github.io/spring/exception-guide/",
                authors = listOf("Judy"),
                categories = listOf("spring", "exception", "java"),
                publishedDateTime = LocalDateTime.of(2019, 11, 26, 0, 0),
            )
        }
    }
}
