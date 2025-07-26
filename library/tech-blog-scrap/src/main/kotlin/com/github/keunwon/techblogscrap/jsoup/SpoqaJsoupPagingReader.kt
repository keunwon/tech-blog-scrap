package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class SpoqaJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://spoqa.github.io/"
        } else {
            "https://spoqa.github.io/page${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("post-author-info")).map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-title-words"))!!.text(),
                    comment = selectFirst(Class("post-description"))!!.text(),
                    url = "https://spoqa.github.io${
                        selectFirst(Class("post-title"))!!.selectFirst(Tag("a"))!!.attr("href").dropWhile { it == '.' }
                    }",
                    authors = selectFirst(Class("author-name"))!!.text().replace(" ", "").split(","),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_KO.convert(selectFirst(Class("post-date"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Evaluator.Id("post-pagination"))!!
            .select(Tag("p"))
            .last()!!
            .classNames().none { it == "disabled" }
    }
}
