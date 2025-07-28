package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import java.time.LocalDateTime

class SkPlanetJsonPagingReaderTest : FunSpec() {
    init {
        test("sk plane 블로그 글 읽기") {
            val reader = SkPlanetJsonPagingReader(
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
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
                title = "SK플래닛 Tech Topic 기술 블로그를 소개합니다!",
                comment = "안녕하세요, 테크편집부입니다! \n2023년 1월, SK플래닛의 기술 블로그를 새롭게 오픈하였습니다. 이름: TECH TOPIC 링크: https://techtopic.skplanet.com/ 한줄설명: \"SK플래닛 구성원의 '기술 활동'을 공유합니다.\"   여기서 '기술 활동' 은 중의적인 의미를 가지는데요. (1) SK…",
                url = "https://techtopic.skplanet.com/skp-techblog-intro/",
                authors = listOf("techeditorial"),
                categories = listOf("DevRel", "개발문화", "TechBlog", "기술블로그"),
                publishedDateTime = LocalDateTime.of(2023, 1, 2, 0, 0),
            )
        }
    }
}
