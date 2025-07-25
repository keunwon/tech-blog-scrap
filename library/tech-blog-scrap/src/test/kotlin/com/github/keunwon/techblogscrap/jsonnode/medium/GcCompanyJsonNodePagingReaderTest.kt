package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class GcCompanyJsonNodePagingReaderTest : FunSpec() {
    init {
        test("여기어때 기술블로그") {
            val path = GcCompanyJsonNodePagingReaderTest::class.java.classLoader
                .getResource("query/gccompany-medium.txt")!!.file

            val reader = GcCompanyJsonNodePagingReader(
                queryPath = path,
                apiTemplate = RestApiTemplate(
                    url = "https://techblog.gccompany.co.kr/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 129
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
