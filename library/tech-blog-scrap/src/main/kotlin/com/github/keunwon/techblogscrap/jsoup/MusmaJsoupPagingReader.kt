package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class MusmaJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://musma.github.io/"
        } else {
            "https://musma.github.io/page${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div[1]/div/div[2]/ul/li").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-link"))!!.text(),
                    comment = selectFirst(Class("post-excerpt"))!!.text(),
                    url = "https://musma.github.io${selectFirst(Class("post-link"))!!.attr("href")}",
                    authors = listOf(selectFirst(Class("post-categories"))!!.selectFirst(Tag("p"))!!.text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH
                        .convert(selectFirst(Class("post-date"))!!.text().split(" ")[0]),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        val (cur, end) = document.selectFirst(Class("page-number"))!!.text()
            .replace("Page ", "")
            .split(" of ")
            .map { it.toInt() }
        return cur < end
    }
}
