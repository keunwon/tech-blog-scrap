package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class BespinglobalJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://blog.bespinglobal.com/"
        } else {
            "https://blog.bespinglobal.com/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div[1]/div/div/main/article").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("entry-title"))!!.text(),
                    comment = selectFirst(Class("entry-summary"))?.text() ?: "",
                    url = selectFirst(Class("entry-title"))!!.selectFirst(Tag("a"))!!.attr("href"),
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_KO.convert(selectFirst(Class("entry-date"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("nav-links"))!!.selectFirst(Class("next")) != null
    }
}
