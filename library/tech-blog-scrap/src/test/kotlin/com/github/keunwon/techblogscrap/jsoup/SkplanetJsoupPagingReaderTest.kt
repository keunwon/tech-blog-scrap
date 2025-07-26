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

class SkplanetJsoupPagingReaderTest : FunSpec() {
    init {
        test("sk plane 블로그 글 읽기") {
            val reader = SkplanetJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 35
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "iOS 환경에서 비디오 최적화 및 성능 개선 사례",
                comment = "대부분의 동영상 어플리케이션에서 구현되어 있는 것처럼, V 컬러링에서도 동영상 렌더링 시 재생 전 회색 (또는 하얀) 화면의 노출을 방지하고자 배경에 미리 썸네일을 깔아 두는 구현 방식을 사용하고 있습니다. 그런데 iOS가 1…",
                url = "https://techtopic.skplanet.com/ios15-rendering/",
                authors = listOf("공자윤"),
                categories = listOf("ios15", "iOS16", "video", "rendering"),
                publishedDateTime = LocalDateTime.of(2023, 1, 26, 0, 0),
            )
        }
    }
}
