package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class YogiyoJsonNodePagingReaderTest : FunSpec() {
    init {
        test("요기요 블로그글 읽기") {
            val reader = YogiyoJsonNodePagingReader(
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/deliverytechkorea/load-more?sortBy=latest",
                    httpMethod = HttpMethod.POST
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThan 100
            posts.forAll { post ->
                post.title.shouldNotBeBlank()
                post.authors.shouldNotBeNull()
                post.url.shouldNotBeBlank()
                post.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
