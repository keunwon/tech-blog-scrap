package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator
import org.jsoup.select.Evaluator.Class

class DanawalabJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://danawalab.github.io/"

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div[1]/div/section/div/div").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("content__h3"))!!.text(),
                    comment = selectFirst(Class("content__p"))!!.text(),
                    url = "https://danawalab.github.io${selectFirst(Evaluator.Tag("a"))!!.attr("href")}",
                    authors = listOf(selectFirst(Class("name"))!!.text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA
                        .convert(selectFirst(Class("date"))!!.text().dropLast(1)),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
