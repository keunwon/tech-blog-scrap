package com.github.keunwon.techblogscrap.jsoup

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual

class SoomgoJsoupPagingReaderTest : FunSpec() {
    init {
        test("숨고 블로그 글 읽기") {
            val reader = SoomgoJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 81
        }
    }
}
