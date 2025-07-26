package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator
import org.jsoup.select.Evaluator.Class

class SquarelabJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://squarelab.co/blog/"
        } else {
            "https://squarelab.co/blog/?page=${page + 1}"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("post-wrap")).map { element ->
            element.run {
                val author = element.selectFirst(Class("author"))!!
                BlogPost(
                    title = selectFirst(Class("ctitle"))!!.text(),
                    comment = selectFirst(Class("excerpt"))!!.text(),
                    url = "https://squarelab.co${selectFirst(Evaluator.Tag("a"))!!.attr("href")}",
                    authors = listOf(author.selectFirst(Evaluator.Tag("small"))!!.text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY
                        .convert(selectFirst(Evaluator.AttributeWithValue("name", "locale-date"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
