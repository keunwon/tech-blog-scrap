package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class HyperconnectJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://hyperconnect.github.io"

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectFirst(Class("post-list"))!!.select(Tag("li"))
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = selectFirst(Class("post-link"))!!.text(),
                    comment = selectFirst(Class("post-excerpt"))!!.text(),
                    url = "https://hyperconnect.github.io${selectFirst(Class("post-link"))!!.attr("href")}",
                    authors = selectFirst(Class("author"))!!.text().split(", "),
                    categories = select(Class("tag")).map { it.text() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH
                        .convert(selectFirst(Class("post-meta"))!!.selectFirst(Tag("time"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
