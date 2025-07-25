package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class DevsistersJsonNodePagingReaderTest : FunSpec() {
    init {
        test("데브시스터즈 블로그 글 읽기") {
            val reader = DevsistersJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://tech.devsisters.com/page-data/index/page-data.json?page=1"),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 64
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "쪼그라드는 웹페이지",
                comment = "모바일 반응형 웹페이지를 쉽고 빠르게 구현하기 위해\n저희가 고안한 방법을 소개합니다.",
                url = "https://tech.devsisters.com/posts/shrinking-webpage",
                authors = listOf("최종찬"),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2019, 2, 26, 0, 0, 0),
            )
        }
    }
}
