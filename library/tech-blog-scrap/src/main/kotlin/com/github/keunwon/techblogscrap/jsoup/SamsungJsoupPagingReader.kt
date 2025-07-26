package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class SamsungJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return "https://techblog.samsung.com/?page=${page + 1}&"
    }

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectXpath("/html/body/main/section/form/div/div[2]/ul/li").dropLast(1)
        return contents.map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Tag("h3"))!!.text(),
                    comment = "",
                    url = "https://techblog.samsung.com${selectFirst(Tag("a"))!!.attr("href")}",
                    authors = listOf(selectFirst(Class("blog-writer"))!!.selectFirst(Class("main-name"))!!.text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY.convert(selectFirst(Class("date"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pagination"))!!
            .select(Tag("a"))
            .takeLast(2)
            .none { element -> element.classNames().any { it == "disabled" } }
    }
}
