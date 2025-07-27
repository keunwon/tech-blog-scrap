package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class ZumInternetJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://zuminternet.github.io/"

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("box-body")).map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-title"))!!.selectFirst(Class("text"))!!.text(),
                    comment = selectFirst(Class("description"))!!.text(),
                    url = "https://zuminternet.github.io${selectFirst(Class("post-link"))!!.attr("href")}",
                    authors = emptyList(),
                    categories = selectFirst(Class("tags"))!!.select(Tag("a")).map { it.text() },
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY.convert(
                        select(Tag("time")).first { el -> el.classNames().any { it == "date" } }!!.text().replace("Mark", "March")
                    ),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
