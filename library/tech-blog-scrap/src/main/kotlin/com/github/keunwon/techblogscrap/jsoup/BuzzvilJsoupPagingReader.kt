package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class BuzzvilJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://tech.buzzvil.com/"
        } else {
            "https://tech.buzzvil.com/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/section[1]/div/div/div/div[1]/div/article").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-title"))!!.text(),
                    comment = selectFirst(Class("card-body"))!!.selectFirst(Tag("p"))!!.text(),
                    url = selectFirst(Class("post-title"))!!.attr("href"),
                    authors = select(Class("card-meta-author")).map { it.text() },
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.DD_MMM_ENG_COMMA_YYYY
                        .convert(selectFirst(Class("card-meta"))!!.select(Tag("li")).last()!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pagination"))!!
            .select(Tag("li"))
            .last()!!
            .classNames().none { it == "active" }
    }
}
