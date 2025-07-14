package com.github.keunwon.techblogscrap.support

import com.github.keunwon.techblogscrap.JPath
import com.github.keunwon.techblogscrap.JsonTagProperty
import com.github.keunwon.techblogscrap.JsoupTagProperty
import com.github.keunwon.techblogscrap.XPath
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.File

class BlogTagYamlParserTest : FunSpec() {
    init {
        test("yaml 파싱") {
            val resource = BlogTagYamlParser::class.java.classLoader.getResource("test-blog.yml")!!
            val blogTagProperties = BlogTagYamlParser.load(File(resource.path))

            val tossJPathProperties = JsonTagProperty(
                name = "토스",
                firstUrl = "https://api-public.toss.im/api-public/v3/ipd-thor/api/v1/workspaces/15/posts?size=20&page=1",
                remainingUrlTemplate = "https://api-public.toss.im/api-public/v3/ipd-thor/api/v1/workspaces/15/posts?size=20&page=%d",
                pageSize = "success.pageSize",
                page = "success.page",
                totalPage = "",
                jPath = JPath(
                    contentPrefix = "success.results",
                    title = "title",
                    comment = "subtitle",
                    url = "",
                    category = "categories.name",
                    author = "editor.name",
                    dateTimeFormat = "yyyy-MM-dd HH:mm:ss",
                    publishedDateTime = "publishedTime",
                ),
            )

            val naverJPathProperties = JsonTagProperty(
                name = "네이버",
                firstUrl = "https://d2.naver.com/api/v1/contents?categoryId=&page=0&size=20",
                remainingUrlTemplate = "https://d2.naver.com/api/v1/contents?categoryId=&page=%d&size=20",
                pageSize = "page.size",
                page = "page.number",
                totalPage = "page.totalPages",
                jPath = JPath(
                    contentPrefix = "content",
                    title = "postTitle",
                    comment = "",
                    url = "https://d2.naver.com{{url}}",
                    category = "",
                    author = "",
                    dateTimeFormat = "timestamp",
                    publishedDateTime = "postPublishedAt"
                )
            )

            val kurlyJsoupTagProperties = JsoupTagProperty(
                name = "마켓컬리",
                firstUrl = "https://helloworld.kurly.com/",
                remainingUrlTemplate = "",
                pageSize = -1,
                page = 1,
                xPath = XPath(
                    content = "/html/body/main/div/div/ul/li",
                    title = "a/h3",
                    comment = "a/p",
                    url = "https://helloworld.kurly.com{{a}}",
                    category = "span/span[1]",
                    author = "span/span[1]",
                    dateTimeFormat = "yyyy.MM.dd",
                    publishedDateTime = "span/span[2]",
                )
            )

            blogTagProperties.json[0] shouldBe tossJPathProperties
            blogTagProperties.json[1] shouldBe naverJPathProperties
            blogTagProperties.jsoup[0] shouldBe kurlyJsoupTagProperties
        }
    }
}
