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

class GMarketJsoupPagingReaderTest : FunSpec() {
    init {
        test("지마켓 블로그글 읽기") {
            val reader = GMarketJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 104
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "Swift에서 String String? String! 의 차이",
                comment = "안녕하세요! BXP POD에서 iOS 개발자로 일하고 있는 강수진입니다. \uD83D\uDE4B\uD83C\uDFFB\u200D♀\uFE0F며칠 전 저희 슬랙 방에서 String, String?, String! 에 대한 팝업 퀴즈가 있었습니다.\"흠..다 비슷하게 생겨서 뭐가 뭔지..\"라고 생각하시는 분들이 있다면 잘 찾아오셨어요! 이 글은 바로 저렇게 생각하던 제가 깨달음을 얻는 과정을 담고 있기 때문이죠.그럼 지금부터 저와 함께 Swift에서 String String? String! 의 차이에 대해 알아보러 가실까요? 퀴즈가 쏘아 올린 작은 공.. \uD83C\uDFD0 평화로운 어느 오후 iOS 워킹 그룹에 슬랙이 울렸습니다. 여러분도 한번 생각해보세요! 3..2..1..!어떤 생각이 떠올랐나요? 우선 제 동료분들의 대답을 살펴볼게요. 저의 대답은 이거였습니다. 여러..",
                url = "https://dev.gmarket.com/10",
                authors = emptyList(),
                categories = listOf("Mobile"),
                publishedDateTime = LocalDateTime.of(2020, 11, 4, 0, 0, 0),
            )
        }
    }
}
