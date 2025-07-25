package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Id
import org.jsoup.select.Evaluator.Tag

class Tech11StJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://11st-tech.github.io/"
        } else {
            "https://11st-tech.github.io/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectFirst(Id("post-list"))!!.children().map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-title"))!!.text(),
                    comment = selectFirst(Class("post-excerpt"))!!.text(),
                    url = "https://11st-tech.github.io${selectFirst(Tag("a"))!!.attr("href")}",
                    authors = listOf(selectFirst(Class("author-name"))!!.text()),
                    categories = select(Class("post-tags")).map { it.text() }.filter { it.isNotBlank() },
                    publishedDateTime = DateTimeOptions
                        .YYYY_MM_DD_DASH.convert(selectFirst(Class("post-date"))!!.text()),
                )
            }
        }
    }

    override fun doNext(document: Document) {
        return
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Id("pagination"))!!
            .selectFirst(Id("page-next"))!!
            .className() != "disabled"
    }
}
