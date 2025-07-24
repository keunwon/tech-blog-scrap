package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class WoowahanJsonNodePagingReaderTest : FunSpec() {
    init {
        val apiTemplate = RestApiTemplate(
            url = "https://techblog.woowahan.com/wp-admin/admin-ajax.php",
            httpMethod = HttpMethod.POST,
        )

        test("우아한형제들 블로그 글 읽기") {
            val reader = WoowahanJsonNodePagingReader(apiTemplate, testObjectMapper)
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 460
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.forSome {
                it.comment.shouldNotBeBlank()
            }
        }
    }
}
