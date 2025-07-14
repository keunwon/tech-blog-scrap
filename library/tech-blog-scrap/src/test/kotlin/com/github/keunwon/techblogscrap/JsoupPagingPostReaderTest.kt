package com.github.keunwon.techblogscrap

import com.github.keunwon.techblogscrap.support.BlogTagYamlParser
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.File
import java.time.LocalDateTime

class JsoupPagingPostReaderTest : FunSpec() {
    private val fileRootPath = JsoupPagingPostReader::class.java.classLoader.getResource("html")!!.path
    private val blogTagProperty = BlogTagYamlParser.load(
        File(JsoupPagingPostReader::class.java.classLoader.getResource("test-blog.yml")!!.path)
    )
    private val pagingTemplate = FilePagingTemplate()

    init {
        test("마켓컬리 블로그 글 읽기") {
            val reader = createJsoupPagingPostReader("마켓컬리")

            val contents = (1..100).mapNotNull { reader.readOrNull() }

            contents.size shouldBe 100
        }

        test("라인 블로그 글 읽기") {
            val reader = createJsoupPagingPostReader("라인")

            val content = (1..12).mapNotNull { reader.readOrNull() }

            content.size shouldBe 12
        }

        test("뱅크샐러드 블로그 글 읽기") {
            val reader = createJsoupPagingPostReader("뱅크샐러드")

            val content = (1..15).mapNotNull { reader.readOrNull() }

            content.size shouldBe 15
        }

        test("쏘카 블로그 글 읽기") {
            val reader = createJsoupPagingPostReader("쏘카")

            val content = (1..5).mapNotNull { reader.readOrNull() }

            content.size shouldBe 5
            content[0].title shouldBe "FE Core팀의 CI 속도전: 캐시 전략을 활용한 병렬 빌드"
            content[0].comment shouldBe "Monorepo 환경에서의 빌드 최적화 사례"
            content[0].authors shouldBe listOf("아놀드")
            content[0].publishedDateTime shouldBe LocalDateTime.of(2025, 6, 10, 0, 0, 0)
        }
    }

    private fun createJsoupPagingPostReader(name: String) = JsoupPagingPostReader(
        pagingQueryProvider = SimplePagingQueryProvider(
            initUrl = "${fileRootPath}/${name}_1.html",
            urlTemplate = "",
        ),
        pagingTemplate = pagingTemplate,
        mapper = JsoupPagingMapper(getJsoupTagProperty(name))
    )

    private fun getJsoupTagProperty(name: String) = blogTagProperty.jsoup.first { it.name == name }
}
