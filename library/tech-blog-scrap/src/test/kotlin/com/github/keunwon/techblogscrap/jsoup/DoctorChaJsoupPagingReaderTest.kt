package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class DoctorChaJsoupPagingReaderTest : FunSpec() {
    init {
        test("오토피디아 블로그 글 읽기") {
            val reader = DoctorChaJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 19
            posts.last() shouldBe BlogPost(
                title = "딥러닝 모델을 활용하여 미응대 상담 알리미 개발하기",
                comment = "상담, 놓치지 않을거에요",
                url = "https://blog.doctor-cha.com/deep-learning-based-counsel-termination-classifier",
                authors = emptyList(),
                categories = listOf("ML"),
                publishedDateTime = LocalDateTime.of(2021, 8, 24, 0, 0),
            )
        }
    }
}
