package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class NetmarbleJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://netmarble.engineering/category/onstage/"
        } else {
            "https://netmarble.engineering/category/onstage/page/${page + 1}/?et_blog"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectXpath("/html/body/article/div/div/div/div/div/div/div[2]/div/div/div/div/article")
        return contents.map { content ->
            content.run {
                selectFirst(Evaluator.AllElements())

                BlogPost(
                    title = selectFirst(Tag("a"))!!.text(),
                    comment = selectFirst(Class("post-content"))!!.selectFirst(Tag("p"))!!.text(),
                    url = selectFirst(Tag("a"))!!.attr("href"),
                    authors = listOf(selectFirst(Class("author vcard"))!!.select(Class("author url fn")).text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH
                        .convert(selectFirst(Class("published"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("nextpostslink")) != null
    }
}
