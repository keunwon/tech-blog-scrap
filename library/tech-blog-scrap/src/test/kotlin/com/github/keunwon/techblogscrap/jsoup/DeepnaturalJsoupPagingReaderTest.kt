package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class DeepnaturalJsoupPagingReaderTest : FunSpec() {
    init {
        test("딥네츄럴 블로그 글 읽기") {
            val reader = DeepnaturalJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 7
            posts.last() shouldBe BlogPost(
                title = "어떤 LLM의 성능이 더 좋은가요 - 챗봇 아레나",
                comment = "Chatbot Arena는 대규모 언어 모델(LLM)을 위한 벤치마킹 플랫폼으로, 실제 시나리오에서 그 기능을 평가하기 위한 고유한 접근 방식을 활용합니다. 다음은 챗봇 아레나의 몇 가지 주요 측면입니다. Chatbot Arena는 보다 실용적이고 사용자 중심적인 방식으로 오픈 소스 LLM을 평가하도록 설계되었습니다. 이는 문제의 개방형 특성과 자동 응답 ...",
                url = "https://deepnatural.ai/ko/blog/which-llm-is-better-chabot-arena-ko",
                authors = listOf("Anson Park"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2023, 1, 9, 0, 0),
            )
        }
    }
}
