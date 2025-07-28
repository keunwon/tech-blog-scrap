package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class SocarJsoupPagingReaderTest : FunSpec() {
    init {
        test("쏘카 기술블로그") {
            val reader = SocarJsoupPagingReader()

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThan 60
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "Keycloak를 이용한 SSO 구축(web + wifi + ssh)",
                comment = "ID 하나로 백오피스 + Wifi + 서버(SSH)에 접속 가능한 환경 구축하기",
                url = "https://tech.socarcorp.kr/security/2019/07/31/keycloak-sso.html",
                authors = listOf("도마"),
                categories = listOf("keycloak", "sso"),
                publishedDateTime = LocalDateTime.of(2019, 7, 31, 0, 0),
            )
        }
    }
}
