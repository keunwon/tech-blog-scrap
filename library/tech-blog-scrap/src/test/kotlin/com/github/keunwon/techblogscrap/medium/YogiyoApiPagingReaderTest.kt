package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class YogiyoApiPagingReaderTest : FunSpec() {
    init {
        test("요기요 기술 블로그 읽기") {
            val reader = YogiyoApiPagingReader(
                objectMapper = testObjectMapper,
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/deliverytechkorea/load-more?sortBy=latest",
                    httpMethod = HttpMethod.POST,
                    headers = mapOf(
                        "content-type" to "application/json",
                        "accept" to "application/json",
                        "x-xsrf-token" to "1",
                    ),
                ),
            )

            val blogPosts = generateSequence { reader.read() }.toList()

            blogPosts.size shouldBeGreaterThan 10
            blogPosts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
