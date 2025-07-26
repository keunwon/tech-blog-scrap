package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe

class MediumPublicationSectionPostsQueryReaderTest : FunSpec() {
    init {
        val resource = MediumPublicationSectionPostsQueryReaderTest::class.java.classLoader
            .getResource("query/publicationSectionPostsQuery.txt")!!.file

        test("크몽 블로그 글 읽기") {
            val reader = MediumPublicationSectionPostsQueryReader(
                mainUrl = "https://blog.kmong.com/subpage/f2333d195b2a",
                queryPath = resource,
                variables = PublicationSectionPostsQuery(),
                apiTemplate = RestApiTemplate(
                    url = "https://blog.kmong.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 106
            posts.last() shouldBe BlogPost(
                title = "크몽 gulp 개선기",
                comment = "build 자동화 도구를 개선할까 말까 고민 중인가요?",
                url = "https://blog.kmong.com/%ED%81%AC%EB%AA%BD-gulp-%EA%B0%9C%EC%84%A0%EA%B8%B0-f9ea41d4ecfb",
                authors = listOf("정백경"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1533887760000L),
            )
        }
    }
}
