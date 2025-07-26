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

class MusmaJsoupPagingReaderTest : FunSpec() {
    init {
        test("무스마 블로그 글 읽기") {
            val reader = MusmaJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 129
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "바야흐로 MUSMA 기술블로그 大-오픈의 시대!",
                comment = "바야흐로 MUSMA 기술블로그 大-오픈의 시대! 안녕하세요! 새해 복 많이 받으세요! \uD83E\uDD17 새해를 맞이하여 늦은 감이 있지만 기술블로그를 오픈하게 됐습니다! (짝짝짝) 저희는 크레인충돌방지시스템 M-CAS, mLoRa를 이용한 통합안전관리시스템 OSS 등의 솔루션으로 산업...",
                url = "https://musma.github.io/2019/01/17/musma-tech-blog.html",
                authors = listOf("송용석 책임"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2019, 1, 17, 0, 0, 0),
            )
        }
    }
}
