package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class NHNCloudJsonNodePagingReaderTest : FunSpec() {
    init {
        test("NHN 기술블로그 글 읽기") {
            val reader = NHNCloudJsonNodePagingReader(
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 100
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.categories.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
