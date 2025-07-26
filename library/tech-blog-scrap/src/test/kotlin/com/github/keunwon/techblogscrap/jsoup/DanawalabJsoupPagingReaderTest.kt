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

class DanawalabJsoupPagingReaderTest : FunSpec() {
    init {
        test("다나와 블로그 글 읽기") {
            val reader = DanawalabJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 118
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "Kubernetes 서비스와 인그레스 용도구분",
                comment = "이번 포스팅에서는 쿠버네티스의 서비스와 인그레스의 차이점에 대해서 알아보겠습니다. 쿠버네티스를 처음 접하는 분들은, 간단하게 API 서버를 만들어서 `POD` 를 띄우기까지는 성공하지만, 해당 IP와 포트로 접속이 안되어 당황을 하게 됩니다.",
                url = "https://danawalab.github.io/kubernetes/2020/01/23/kubernetes-service-ingress.html",
                authors = listOf("송상욱"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2020, 1, 23, 0, 0, 0),
            )
        }
    }
}
