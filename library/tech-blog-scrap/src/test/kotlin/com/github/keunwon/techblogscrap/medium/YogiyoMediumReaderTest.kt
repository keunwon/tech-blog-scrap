package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.generateDriver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

class YogiyoMediumReaderTest : FunSpec() {
    init {
        test("요기요 블로그 글 읽기") {
            val yogiyoReader = YogiyoMediumSeleniumReader(generateDriver())

            val actual = yogiyoReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual.forEach {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.authors.isNotEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}
