package com.github.keunwon.techblogscrap

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class JsonPagingPostReaderTest : FunSpec() {
    init {
        val resource = JsonPagingPostReader::class.java.classLoader.getResource("json")!!.path

        test("토스 블로그 글 읽기") {
            val jsonPagingPostReader = JsonPagingPostReader(
                pagingQueryProvider = SimplePagingQueryProvider(
                    initUrl = "${resource}/토스_1.json",
                    urlTemplate = "${resource}/토스_%d.json",
                ),
                pagingTemplate = FilePagingTemplate(),
                mapper = JsonPagingMapper(TOSS_JSON_Tag_PROPERTIES, testObjectMapper),
            )

            val contents = (1..10).mapNotNull { jsonPagingPostReader.readOrNull() }

            contents.size shouldBe 10
        }

        test("네이버 블로그 글 읽기") {
            val jsonPagingPostReader = JsonPagingPostReader(
                pagingQueryProvider = SimplePagingQueryProvider(
                    initUrl = "${resource}/네이버_1.json",
                    urlTemplate = "${resource}/토스_%d.json",
                ),
                pagingTemplate = FilePagingTemplate(),
                mapper = JsonPagingMapper(NAVER_JSON_Tag_PROPERTIES, testObjectMapper),
            )

            val content = (1..20).mapNotNull { jsonPagingPostReader.readOrNull() }

            content.size shouldBe 20
            content[0].title shouldBe "서비스 조직에서 Kafka를 사용할 때 알아 두어야 할 것들 (4)"
        }
    }
}
