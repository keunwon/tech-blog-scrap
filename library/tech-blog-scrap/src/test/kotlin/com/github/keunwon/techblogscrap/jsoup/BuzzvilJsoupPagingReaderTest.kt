package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forSome
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class BuzzvilJsoupPagingReaderTest : FunSpec() {
    init {
        test("버즈빌 블로그 글 읽기") {
            val reader = BuzzvilJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThanOrEqual 87
            posts.forSome {
                it.authors.size shouldBeGreaterThan 1
            }
            posts.last() shouldBe BlogPost(
                title = "Scaling PhantomJS With Ghost Town",
                comment = "My first project at Buzzvil was to develop a system to reliably render images at scale with …",
                url = "https://tech.buzzvil.com/blog/scaling-phantomjs-ghost-town/",
                authors = emptyList(),
                categories = emptyList(),
                publishedDateTime = LocalDateTime.of(2014, 5, 29, 0, 0),
            )
        }
    }
}
