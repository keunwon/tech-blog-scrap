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

class KakaobankJsoupPagingReaderTest : FunSpec() {
    init {
        test("카카오뱅크 블로그 글 읽기") {
            val reader = KakaobankJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 37
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "Kode Runner 2022를 회고합니다.",
                comment = "안녕하세요, 기술XR팀의 Celina입니다. 저는 카카오뱅크의 사내 기술 컨퍼런스인 Kode Runner를 준비하며, 1년 전 개최된 Kode Runner 2022를 회고하고자 합니다. 이번 글에서는 DevRel의 중요성과 Kode Runner의 기획 배경, 주요 행사 내용 및 다양한 이벤트들에 대해 소개합니다. 카카오뱅크의 기술 문화와 개발자들의 성장을 지원하는 DevRel 활동에 관심이 있는 분들에게 유익한 글이 될 것입니다.",
                url = "https://tech.kakaobank.com/posts/2307-koderunner-2022-reflection/",
                authors = listOf("Celina(신유라)"),
                categories = listOf("DevRel", "Event", "Conference"),
                publishedDateTime = LocalDateTime.of(2023, 7, 15, 0, 0, 0),
            )
        }
    }
}
