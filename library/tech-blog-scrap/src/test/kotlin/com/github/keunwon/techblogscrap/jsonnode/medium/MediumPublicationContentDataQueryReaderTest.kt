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
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }

        test("헤이딜러 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofSlug("prnd"),
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBe 37
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "광고비로 돈을 많이 썼는데 왜 실제 사용자는 늘어 나지 않을까?",
                comment = "안녕하세요. 저는 안드로이드 팀의 박상권입니다.",
                url = "https://medium.com/prnd/%ED%81%B4%EB%A6%AD-%EC%9D%B8%EC%A0%9D%EC%85%98-%EA%B4%91%EA%B3%A0%EB%B9%84%EB%A1%9C-%EB%8F%88%EC%9D%84-%EB%A7%8E%EC%9D%B4-%EC%8D%BC%EB%8A%94%EB%8D%B0-%EC%99%9C-%EC%8B%A4%EC%A0%9C-%EC%82%AC%EC%9A%A9%EC%9E%90%EB%8A%94-%EB%8A%98%EC%96%B4-%EB%82%98%EC%A7%80-%EC%95%8A%EC%9D%84%EA%B9%8C-687ba555588f",
                authors = listOf("Ted Park"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1535698480864L),
            )
        }

        test("테이블링 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofDomain("techblog.tabling.co.kr"),
                apiTemplate = RestApiTemplate(
                    url = "https://techblog.tabling.co.kr/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 30
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "테이블링 백엔드 기술 스택",
                comment = "백엔드 팀에서 사용하고 있는 기술을 간단히 소개합니다.",
                url = "https://techblog.tabling.co.kr/%ED%85%8C%EC%9D%B4%EB%B8%94%EB%A7%81-%EB%B0%B1%EC%97%94%EB%93%9C-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D-6f9b6d473a0f",
                authors = listOf("김승기"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1641200248164L),
            )
        }

        test("오일나우 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofSlug("오일나우-팀-블로그"),
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 25
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "소규모 스타트업에서 OKR 도입은 어떤 결과를 가져다 주었을까?",
                comment = "스타트업은 하루를 걸러 수도 없는 결정 사항들이 정해지고 수정하기가 반복된다.",
                url = "https://medium.com/%EC%98%A4%EC%9D%BC%EB%82%98%EC%9A%B0-%ED%8C%80-%EB%B8%94%EB%A1%9C%EA%B7%B8/%EC%86%8C%EA%B7%9C%EB%AA%A8-%EC%8A%A4%ED%83%80%ED%8A%B8%EC%97%85%EC%97%90%EC%84%9C-okr-%EB%8F%84%EC%9E%85%EC%9D%80-%EC%96%B4%EB%96%A4-%EA%B2%B0%EA%B3%BC%EB%A5%BC-%EA%B0%80%EC%A0%B8%EB%8B%A4-%EC%A3%BC%EC%97%88%EC%9D%84%EA%B9%8C-f6bdc26a1352",
                authors = listOf("김태성"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1583887392579L),
            )
        }

        test("펫프렌즈 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofDomain("techblog.pet-friends.co.kr"),
                apiTemplate = RestApiTemplate(
                    url = "https://techblog.pet-friends.co.kr/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 36
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
            posts.last() shouldBe BlogPost(
                title = "최선의 해결책을 같이 찾는 CTO 남경식(제스)입니다",
                comment = "안녕하세요. 최선의 해결책을 같이 찾아가는 펫프렌즈 CTO 남경식(제스) 입니다. 펫프렌즈 기술팀에 대해 자세히 소개드립니다",
                url = "https://techblog.pet-friends.co.kr/%EC%B5%9C%EC%84%A0%EC%9D%98-%ED%95%B4%EA%B2%B0%EC%B1%85%EC%9D%84-%EA%B0%99%EC%9D%B4-%EC%B0%BE%EB%8A%94-cto-%EB%82%A8%EA%B2%BD%EC%8B%9D-%EC%A0%9C%EC%8A%A4-%EC%9E%85%EB%8B%88%EB%8B%A4-5b3982f36d7c",
                authors = listOf("제스(Jess/남경식)"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1681891900307),
            )
        }

        test("당근 블로그 글 읽기") {
            val reader = MediumPublicationContentDataQueryReader(
                queryPath = resource,
                variables = PublicationContentDataQuery.ofSlug("daangn"),
                apiTemplate = RestApiTemplate(
                    url = "https://medium.com/_/graphql",
                    httpMethod = HttpMethod.POST,
                ),
                objectMapper = testObjectMapper,
            )

            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 178
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.authors.shouldNotBeEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }.forSome {
                it.comment.shouldNotBeBlank()
            }
            posts.last() shouldBe BlogPost(
                title = "미국 십대가 본 SNS 서비스",
                comment = "아래 원문글을 요약 번역한 글입니다.",
                url = "https://medium.com/daangn/%EB%AF%B8%EA%B5%AD-%EC%8B%AD%EB%8C%80%EA%B0%80-%EB%B3%B8-sns-%EC%84%9C%EB%B9%84%EC%8A%A4-2d790484aa42",
                authors = listOf("Gary Kim"),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1433494216784L),
            )
        }
    }
}
