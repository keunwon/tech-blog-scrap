package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator

class KurlyJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://helloworld.kurly.com"

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectXpath("/html/body/main/div/div/ul/li")
        return contents.map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Evaluator.Class("post-title"))!!.text(),
                    comment = selectFirst(Evaluator.Class("title-summary"))?.text() ?: "",
                    url = selectFirst(Evaluator.Class("post-link"))!!.attr("href"),
                    authors = selectFirst(Evaluator.Class("post-autor"))!!.text().split(" "),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA
                        .convert(selectFirst(Evaluator.Class("post-date"))!!.text().dropLast(1)),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
