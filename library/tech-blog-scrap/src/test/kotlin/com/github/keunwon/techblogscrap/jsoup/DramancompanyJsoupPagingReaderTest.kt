package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class DramancompanyJsoupPagingReaderTest : FunSpec() {
    init {
        test("드라마앤컴퍼니 블로그 글 읽기") {
            val reader = DramancompanyJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 59
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "드라마앤컴퍼니 기술 블로그에 오신 것을 환영합니다.",
                comment = "",
                url = "https://dramancompany.github.io/tech-blog/2015/10/05/%EB%93%9C%EB%9D%BC%EB%A7%88%EC%95%A4%EC%BB%B4%ED%8D%BC%EB%8B%88-%EA%B8%B0%EC%88%A0-%EB%B8%94%EB%A1%9C%EA%B7%B8%EC%97%90-%EC%98%A4%EC%8B%A0-%EA%B2%83%EC%9D%84-%ED%99%98%EC%98%81%ED%95%A9%EB%8B%88.html",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2015, 10, 5, 0, 0)
            )
        }
    }
}
