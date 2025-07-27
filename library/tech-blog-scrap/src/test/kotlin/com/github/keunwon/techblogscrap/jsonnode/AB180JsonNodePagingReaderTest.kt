package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe

class AB180JsonNodePagingReaderTest : FunSpec() {
    init {
        test("AB180 블로그 글 읽기") {
            val reader = AB180JsonNodePagingReader(
                apiTemplate = RestApiTemplate(
                    url = "https://oopy.lazyrockets.com/api/v2/notion/queryCollection?src=reset",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 48
            posts.last() shouldBe BlogPost(
                title = "Amazon EKS 위에 Kiam 셋업하기",
                comment = "",
                url = "https://engineering.ab180.co/fe41b093-efba-4471-9374-9a027cd08a2b",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1599819330821L),
            )
        }
    }
}
