package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class BespinglobalJsoupPagingReaderTest : FunSpec() {
    init {
        test("베스핀글로벌 블로그 글 읽기") {
            val reader = BespinglobalJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 521
            posts.last() shouldBe BlogPost(
                title = "[AWS 한국 블로그] AWS 기반 게임 개발자를 위한 안내서 – DDoS 공격 방어",
                comment = "안녕하세요!베스핀글로벌 클라우드 기술지원팀입니다. \uD83D\uDE00 이번 아티클은 AWS 한국 블로그에 기재된 “분산 서비스 거부 (Distributed Denial of Service, DDoS)에 대한 AWS 기반 대응 방법”에 대하여 소개해 드리고자 합니다. 1. DDoS 방어를 위해 제공되는 AWS 서비스 많은 게임사가 AWS를 통해 글로벌하게 게임을 운영하고 있습니다. 그 과정에서 AWS는 많고 다양한 유형의 DDoS 공격을 경험하였으며, 해당 경험을 통해 DDoS 방어에 … Read more",
                url = "https://blog.bespinglobal.com/post/aws-ddos-attack-defense/",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2021, 6, 18, 0, 0),
            )
        }
    }
}
