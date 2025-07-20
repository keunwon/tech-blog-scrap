package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.generateDriver
import com.github.keunwon.techblogscrap.generateHeadlessDriver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class MediumPagingReaderTest : FunSpec() {
    init {
        test("왓챠 블로그 글 읽기") {
            val watchaReader = generateMediumReader(
                url = "https://medium.com/@watcha",
                name = "왓챠",
            )

            val actual = watchaReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual[0].title.shouldNotBeBlank()
            actual[0].comment.shouldNotBeBlank()
            actual[0].publishedDateTime.shouldNotBeNull()
        }

        test("무신사 블로그 글 읽기") {
            val musinsaReader = generateMediumReader(
                url = "https://medium.com/@musinsa-tech",
                name = "무신사",
            )

            val actual = musinsaReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual[0].title.shouldNotBeBlank()
            actual[0].comment.shouldNotBeBlank()
            actual[0].publishedDateTime.shouldNotBeNull()
        }

        test("요기요 블로그 글 읽기") {
            val yogiyoReader = YogiyoMediumReader(generateDriver())

            val actual = yogiyoReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual[0].title.shouldNotBeBlank()
            actual[0].comment.shouldNotBeBlank()
            actual[0].publishedDateTime.shouldNotBeNull()
        }
    }

    private fun generateMediumBrowserReader(url: String, name: String) =
        DefaultMediumPagingReader(generateDriver(), url, name)

    private fun generateMediumReader(url: String, name: String) =
        DefaultMediumPagingReader(generateHeadlessDriver(), url, name)
}
