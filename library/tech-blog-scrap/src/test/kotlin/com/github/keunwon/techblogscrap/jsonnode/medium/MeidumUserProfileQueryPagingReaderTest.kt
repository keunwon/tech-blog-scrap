package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class MeidumUserProfileQueryPagingReaderTest : FunSpec() {
    init {
        test("무신사 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReaderByUsername("musinsa-tech")
            val posts = generateSequence { reader.read() }.toList()

            posts.shouldAll(
                minSize = 34,
                last = BlogPost(
                    title = "무신사 기술 블로그를 시작합니다.",
                    comment = "국내에서 가장 큰 온라인 패션 플랫폼으로 성장하는 과정에서 맞닥뜨린 기술적 이슈와 개선 경험, 의사결정 과정에 대해 공유합니다.",
                    url = "https://medium.com/musinsa-tech/%EB%AC%B4%EC%8B%A0%EC%82%AC-%EA%B8%B0%EC%88%A0-%EB%B8%94%EB%A1%9C%EA%B7%B8%EB%A5%BC-%EC%8B%9C%EC%9E%91%ED%95%A9%EB%8B%88%EB%8B%A4-a6eedad85455",
                    authors = listOf("musinsa-tech"),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1620788131235L),
                )
            )
        }

        test("왓챠 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReaderByUsername("watcha")
            val posts = generateSequence { reader.read() }.toList()

            posts.shouldAll(
                minSize = 29,
                last = BlogPost(
                    title = "장난인 줄 알았죠? ‘고기서 고기’ 탄생기",
                    comment = "만우절에 공개된\n어떻게 탄생했나 궁금하셨죠?\n마케팅팀의 추억 더듬기, 확인해보세요",
                    url = "https://medium.com/watcha/%EC%9E%A5%EB%82%9C%EC%9D%B8-%EC%A4%84-%EC%95%8C%EC%95%98%EC%A3%A0-%EA%B3%A0%EA%B8%B0%EC%84%9C-%EA%B3%A0%EA%B8%B0-%ED%83%84%EC%83%9D%EA%B8%B0-aeb17e6dee09",
                    authors = listOf("watcha"),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1572428175631L),
                )
            )
        }

        test("코인원 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReaderByUsername("coinone")
            val posts = generateSequence { reader.read() }.toList()

            posts.shouldAll(
                minSize = 10,
                last = BlogPost(
                    title = "안녕하세요, 코인원 기술 블로그에 오신 것을 환영합니다.",
                    comment = """“코인원이 기술 블로그를 오픈합니다”""",
                    url = "https://medium.com/coinone/%EC%95%88%EB%85%95%ED%95%98%EC%84%B8%EC%9A%94-%EC%BD%94%EC%9D%B8%EC%9B%90-%EA%B8%B0%EC%88%A0-%EB%B8%94%EB%A1%9C%EA%B7%B8%EC%97%90-%EC%98%A4%EC%8B%A0-%EA%B2%83%EC%9D%84-%ED%99%98%EC%98%81%ED%95%A9%EB%8B%88%EB%8B%A4-5ac36df84b0d",
                    authors = listOf("coinone"),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1551263684189L)
                ),
            )
        }

        test("레몬에이드 블로그 글 읽기") {
            val reader = generateMediumJsonNodePagingReaderById("ce146a86b5e9")
            val posts = generateSequence { reader.read() }.toList()

            posts.shouldAll(
                minSize = 15,
                last = BlogPost(
                    title = "Github action 사용해서 슬랙에 멘션 노티 보내기",
                    comment = "by Siwoo",
                    url = "https://medium.com/lemonade-engineering/github-action-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%84%9C-%EC%8A%AC%EB%9E%99%EC%97%90-%EB%A9%98%EC%85%98-%EB%85%B8%ED%8B%B0-%EB%B3%B4%EB%82%B4%EA%B8%B0-55e1ed64d",
                    authors = listOf("lemonade-engineering"),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1612492469001L)
                ),
            )
        }
    }

    private val apiTemplate =
        RestApiTemplate("https://medium.com/_/graphql", HttpMethod.POST)
    private val queryPath =
        MeidumUserProfileQueryPagingReaderTest::class.java.classLoader.getResource("query/UserProfileQuery.txt")!!.file

    private fun generateMediumJsonNodePagingReaderByUsername(username: String) =
        MeidumUserProfileQueryPagingReader(
            queryPath = queryPath,
            variables = UserProfileQuery.ofUserName(username),
            apiTemplate = apiTemplate,
            objectMapper = testObjectMapper,
        )

    private fun generateMediumJsonNodePagingReaderById(id: String) =
        MeidumUserProfileQueryPagingReader(
            queryPath = queryPath,
            variables = UserProfileQuery.ofId(id),
            apiTemplate = apiTemplate,
            objectMapper = testObjectMapper,
        )

    private fun List<BlogPost>.shouldAll(minSize: Int, last: BlogPost) {
        size shouldBeGreaterThanOrEqual minSize
        forAll {
            it.title.shouldNotBeBlank()
            it.publishedDateTime.shouldNotBeNull()
        }
        last() shouldBe last
    }
}
