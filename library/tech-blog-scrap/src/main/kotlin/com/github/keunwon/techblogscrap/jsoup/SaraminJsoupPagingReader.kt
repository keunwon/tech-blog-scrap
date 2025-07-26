package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class SaraminJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://saramin.github.io/"
        } else {
            "https://saramin.github.io/page${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div[2]/div/div/div/article").map { element ->
            element.run {
                val (names, datetime) = selectFirst(Class("post-meta"))!!
                    .text()
                    .replace("Posted by ", "")
                    .split(" on ")

                BlogPost(
                    title = selectFirst(Class("post-title"))!!.text(),
                    comment = selectFirst(Class("post-subtitle"))?.text() ?: "",
                    url = "https://saramin.github.io${selectFirst(Tag("a"))!!.attr("href")}",
                    authors = names.split(", "),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY.convert(datetime),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pager main-pager"))!!.selectFirst(Class("next")) != null
    }
}
