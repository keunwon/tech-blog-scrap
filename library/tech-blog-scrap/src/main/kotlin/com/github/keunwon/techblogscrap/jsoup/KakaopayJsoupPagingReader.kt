package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class KakaopayJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return "https://tech.kakaopay.com/page/${page + 1}/"
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div/main/div/div[2]/div/ul/li").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Tag("strong"))!!.text(),
                    comment = selectFirst(Tag("p"))!!.text(),
                    url = selectFirst(Tag("a"))!!.attr("href"),
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA
                        .convert(selectFirst(Tag("time"))!!.text().replace(" ", ""))
                )
            }
        }
    }

    override fun doNext(document: Document) {
        return
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pagination"))!!.children().last()!!.tagName() == "a"
    }
}
