package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class TossJsonNodePagingReaderTest : FunSpec() {
    init {
        test("토스 블로 글 읽기") {
            val reader = TossJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://api-public.toss.im"),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 338
            posts.last() shouldBe BlogPost(
                title = "토스 프론트엔드 챕터를 소개합니다!",
                comment = "토스에서 프론트엔드 개발자가 일하는 방법과 맡고 있는 역할에 대해 소개드립니다.",
                url = "https://toss.tech/article/toss-frontend-chapter",
                authors = listOf("박서진"),
                categories = listOf("Frontend"),
                publishedDateTime = LocalDateTime.of(2021, 4, 28, 8, 0, 0),
            )
        }
    }
}
