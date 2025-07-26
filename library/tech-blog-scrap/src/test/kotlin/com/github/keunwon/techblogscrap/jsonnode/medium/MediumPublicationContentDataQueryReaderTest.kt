package com.github.keunwon.techblogscrap.jsonnode.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.HttpMethod
import com.github.keunwon.techblogscrap.RestApiTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forSome
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class MediumPublicationContentDataQueryReaderTest : FunSpec() {
    init {
        val resource = MediumPublicationContentDataQueryReaderTest::class.java.classLoader
            .getResource("query/publicationContentDataQuery.txt")!!.file

        test("루닛 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofSlug("lunit"),
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 86
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "당신에게 Redux는 필요 없을지도 모릅니다.",
                comment = "이 글은 Dan Abramov의 You Might Not Need Redux를 번역한 글입니다.",
                url = "https://medium.com/lunit/%EB%8B%B9%EC%8B%A0%EC%97%90%EA%B2%8C-redux%EB%8A%94-%ED%95%84%EC%9A%94-%EC%97%86%EC%9D%84%EC%A7%80%EB%8F%84-%EB%AA%A8%EB%A6%85%EB%8B%88%EB%8B%A4-b88dcd175754",
                authors = listOf("Sangyeop Bono Yu"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1491374148099L),
            )
        }

        test("직방 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofSlug("zigbang"),
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 78
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "iOS 14 대응하는 이미지 피커 만들기",
                comment = "목차",
                url = "https://medium.com/zigbang/ios-14-%EB%8C%80%EC%9D%91%ED%95%98%EB%8A%94-%EC%9D%B4%EB%AF%B8%EC%A7%80-%ED%94%BC%EC%BB%A4-%EB%A7%8C%EB%93%A4%EA%B8%B0-a75bec193d2f",
                authors = listOf("최동호"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1617247066694L),
            )
        }

        test("롯데-on 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofDomain("techblog.lotteon.com"),
                apiTemplate = RestApiTemplate(
                    url = "https://techblog.lotteon.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 81
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "Jenkins로 서버 접속 없이 CI/CD 통합하기",
                comment = "서버 직접 접속 없이 Jenkins만으로 Batch 코드 빌드(CI)와 실행 및 로그 확인(CD) 시스템 구축 (+캘린더 뷰)",
                url = "https://techblog.lotteon.com/d-142cbbd5010",
                authors = listOf("김민우"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1649005183306L),
            )
        }

        test("모두의 싸인 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofDomain("team.modusign.co.kr"),
                apiTemplate = RestApiTemplate(
                    url = "https://team.modusign.co.kr/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 18
        }
    }
}
