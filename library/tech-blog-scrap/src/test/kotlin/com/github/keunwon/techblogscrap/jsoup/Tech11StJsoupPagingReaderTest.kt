package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class Tech11StJsoupPagingReaderTest : FunSpec() {
    init {
        test("11번가 블로그 글 읽기") {
            val reader = Tech11StJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 27
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.categories.shouldNotBeEmpty()
            }
            posts.last() shouldBe BlogPost(
                title = "11번가 개발조직 소개",
                comment = "들어가며 안녕하세요 저는 11번가 Portal개발그룹 백명석입니다. 2018년 9월 분사한 이후, 공채 개발자분들과 입사 6개월 정도 후에 함께 식사를 하고 차를 마시는 시간을 마련했습니다. 이 시간을 통해 새로 11번가에 합류하신 분들이 어떤 생각을 가지고 계신지 들어보는 시간을 가져왔습니다. 19년 공채분들과 시간을 가졌을 때 저는 “11번가에 무엇을 기대하고 입사를 했고, 6개월 지나 보니 어떤가?”라는 질문을 했었습니다. 그중 2가지 답변이 인상적이었습니다. 별 기대 없이 입사를 했는데 팀과 개발 조직의 문화가 좋아서 만족한다. 이 답변에 저는 “왜 별 기대를...",
                url = "https://11st-tech.github.io/2021/06/05/introduce-dev-group/",
                authors = listOf("백명석"),
                categories = listOf(),
                publishedDateTime = LocalDateTime.of(2021, 6, 5, 0, 0, 0),
            )
        }
    }
}
