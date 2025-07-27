package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class TadaJsonNodePagingReaderTest : FunSpec() {
    init {
        test("타다 블로그 글 읽기") {
            val reader = TadaJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://blog-tech.tadatada.com"),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 30
            posts.last() shouldBe BlogPost(
                title = "VCNC 엔지니어링 블로그를 시작합니다.",
                comment = "",
                url = "https://blog-tech.tadatada.com/2013-04-15-hello-world",
                authors = emptyList(),
                categories = listOf("VCNC", "Engineering", "Blog", "Between", "Developer", "비트윈", "개발자", "블로그"),
                publishedDateTime = LocalDateTime.of(2013, 4, 15, 1, 0, 0, 0),
            )
        }
    }
}
