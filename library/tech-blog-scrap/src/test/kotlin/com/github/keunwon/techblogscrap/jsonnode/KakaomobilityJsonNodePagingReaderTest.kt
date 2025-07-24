package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.GetApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class KakaomobilityJsonNodePagingReaderTest : FunSpec() {
    init {
        test("카카오모빌리티 블로그 글 읽기") {
            val reader = KakaomobilityJsonNodePagingReader(
                apiTemplate = GetApiTemplate("https://developers.kakaomobility.com/api/techblogs"),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 9
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
