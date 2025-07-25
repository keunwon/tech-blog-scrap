package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class OliveyoungJsonNodePagingReaderTest : FunSpec() {
    init {
        test("올리브영 블로그글 읽기") {
            val reader = OliveyoungJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://oliveyoung.tech"),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 142
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.categories.shouldNotBeNull()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "올리브영 기술블로그 개발기",
                comment = "Github Pages + Jekyll 을 사용하여 쉽고 빠르게 팀 블로그 구축하기",
                url = "https://oliveyoung.tech/2020-11-09/How-to-Develop-Blog-With-Github-And-Jekyll/",
                authors = listOf(),
                categories = listOf("blog", "web", "Front-end"),
                publishedDateTime = LocalDateTime.of(2020, 11, 9, 0, 0, 0),
            )
        }
    }
}
