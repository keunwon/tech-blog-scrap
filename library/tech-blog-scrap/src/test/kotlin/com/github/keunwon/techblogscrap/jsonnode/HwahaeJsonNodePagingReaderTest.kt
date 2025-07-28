package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class HwahaeJsonNodePagingReaderTest : FunSpec() {
    init {
        test("화해 블로그 글 읽기") {
            val reader = HwahaeJsonNodePagingReader(
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 86
            posts.last() shouldBe BlogPost(
                title = "화해, ‘AWS 서밋 2019 서울’에 가다!",
                comment = "",
                url = "https://blog.hwahae.co.kr/all/hwahaeteam/culture/606/",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2019, 8, 5, 10, 55, 21),
            )
        }
    }
}
