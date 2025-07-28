package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ToastUIJsonNodePagingReaderTest : FunSpec() {
    init {
        test("ToastUI 블로그 글 읽기") {
            val reader = ToastUIJsonNodePagingReader(
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 307
            posts.last() shouldBe BlogPost(
                title = "Javascript의 커플링 측정",
                comment = "이 글은 커플링을 Javascript기반 예제를 통해 설명한다. 먼저 커플링이란 서로 다른 객체 또는 모듈간의 관계를 뜻한다. 그리고 그 관계의 방법은 조금씩은 다르지만 거의 유사한 패턴이므로 측정도 가능하다. Norman Fenton과 Shari Lawrence Pfleeger가 1996년도에 저술한 \"A Rigorous & Practical Approach, 2nd Edition\" 에 따르면 커플링은 6단계가 있고 각 단계에 점수를 매기는 것으로 측정할 수 있다.",
                url = "https://ui.toast.com/posts/ko_20150522",
                authors = listOf("김민형"),
                categories = listOf("design pattern", "ecmascript", "testing"),
                publishedDateTime = LocalDateTime.of(2015, 5, 22, 0, 0),
            )
        }
    }
}
