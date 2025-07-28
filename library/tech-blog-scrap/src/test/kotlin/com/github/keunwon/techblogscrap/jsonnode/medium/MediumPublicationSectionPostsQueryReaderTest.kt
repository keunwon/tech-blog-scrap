package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe

class MediumPublicationSectionPostsQueryReaderTest : FunSpec() {
    init {
        test("크몽 블로그 글 읽기") {
            val reader = MediumPublicationSectionPostsQueryReader(
                mainUrl = "https://blog.kmong.com/subpage/f2333d195b2a",
                url = "https://blog.kmong.com/_/graphql",
                query = MediumQuery.PUBLICATION_SECTION_POSTS_QUERY,
                variables = PublicationSectionPostsQuery(),
                apiTemplate = testApiJsonNodeTemplate,
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

        test("쿠팡 블로그 글 읽기") {
            val reader = MediumPublicationSectionPostsQueryReader(
                mainUrl = "https://medium.com/coupang-engineering/subpage/cafd0402c284",
                url = "https://medium.com/_/graphql",
                query = MediumQuery.PUBLICATION_SECTION_POSTS_QUERY,
                variables = PublicationSectionPostsQuery(),
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 25
            posts.last() shouldBe BlogPost(
                title = "쿠팡의 디자인 시스템을 소개합니다",
                comment = "높은 업무 효율성과 일관된 사용자 경험을 위한 디자인 시스템 속으로",
                url = "https://medium.com/coupang-engineering/introducing-coupangs-design-system-baeb117949f1",
                authors = listOf("쿠팡 엔지니어링"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1659612928443L),
            )
        }
    }
}
