package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ZumInternetJsoupPagingReaderTest : FunSpec() {
    init {
        test("줌 인터넷 블로그 글 읽기") {
            val reader = ZumInternetJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBe 51
            posts.last() shouldBe BlogPost(
                title = "Spring Boot로 TEAMUP(회사 메신져) BOT 만들기 - (1)",
                comment = "Spring Boot로 TEAMUP(사내 메신져) BOT 뼈대 만들기 Part1!",
                url = "https://zuminternet.github.io/TEAMUP-BOT-START/",
                authors = emptyList(),
                categories = listOf("springboot", "bot", "spring", "teamup"),
                publishedDateTime = LocalDateTime.of(2016, 10, 13, 0, 0),
            )
        }
    }
}
