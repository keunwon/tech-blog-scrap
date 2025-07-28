package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class SocarJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://tech.socarcorp.kr/posts/"
        } else {
            "https://tech.socarcorp.kr/posts/page${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div/div/div/article").map { content ->
            content.run {
                BlogPost(
                    title = selectFirst(Class("post-title"))!!.text(),
                    comment = selectFirst(Class("post-subtitle"))!!.text(),
                    url = "https://tech.socarcorp.kr${selectFirst(Tag("a"))!!.attr("href")}",
                    authors = select(Class("author")).map { it.text() },
                    categories = select(Class("tag")).map { it.text() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH.convert(selectFirst(Class("date"))!!.text())
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("float-right")) != null
    }
}
