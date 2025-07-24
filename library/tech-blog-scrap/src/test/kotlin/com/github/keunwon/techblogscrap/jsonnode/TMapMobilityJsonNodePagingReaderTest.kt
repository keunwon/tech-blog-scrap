package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class TMapMobilityJsonNodePagingReaderTest : FunSpec() {
    init {
        test("티맵 블로그 글 읽기") {
            val reader = TMapMobilityJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://api.brunch.co.kr/v2/article/@tmapmobility"),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 33
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
