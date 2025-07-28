package com.github.keunwon.techblogscrap.jsonnode

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.testApiJsonNodeTemplate
import com.github.keunwon.techblogscrap.testObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class BrunchReaderTest : FunSpec() {
    init {
        test("티맵 블로그 글 읽기") {
            val reader = BrunchReader(
                domain = "https://api.brunch.co.kr",
                name = "tmapmobility",
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 33
            posts.forAll {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.url.shouldNotBeBlank()
                it.publishedDateTime.shouldNotBeNull()
            }
        }

        test("부스터스 블로그 글 읽기") {
            val reader = BrunchReader(
                domain = "https://api.brunch.co.kr",
                name = "boosters",
                apiTemplate = testApiJsonNodeTemplate,
                objectMapper = testObjectMapper,
            )
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 84
            posts.last() shouldBe BlogPost(
                title = "브랜드의 A to Z 를 책임집니다",
                comment = "부스터스의 핵심 사업인 브랜드 사업부를 총괄하고 계신 CMO 최진영님을 소개합니다. 매일 다양한 브랜드가 쏟아져 나오는 치열한 커머스 업계에서 부스터스만의 강점을 찾아 부침 없는 성장을 이끌고 계신 진영님이신데요. 일할 때 있어서 가장 중요한 가치를 '직무 전문성'과 ‘배우려는 열린 자세’로 꼽으셨습니다. 탄탄한 마케팅 및 세일즈 조직을 구축해 진영님 자신",
                url = "https://brunch.co.kr/@boosters/5",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = DateTimeOptions.EPOCH_MILLI.convert(1672302718897L)
            )
        }
    }
}
