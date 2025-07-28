package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Tag

class SoomgoJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://blog.soomgo.com/page/${page + 1}"

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div[1]/main/div/div/section[1]/ul/li").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Tag("h2"))!!.text(),
                    comment = selectFirst(Tag("h3"))!!.text(),
                    url = "https://blog.soomgo.com${selectFirst(Tag("a"))!!.attr("href")}",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = null,
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectXpath("/html/body/div[1]/main/div/div/section[2]/div/a")
            .last()!!
            .childrenSize() > 0
    }
}
