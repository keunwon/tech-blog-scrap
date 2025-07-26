package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.JsoupPagingReader
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class Com2usJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://on.com2us.com/tag/기술이야기/"
        } else {
            "https://on.com2us.com/tag/기술이야기/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/main/section/div/div/div").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Tag("h4"))!!.text(),
                    comment = "",
                    url = URLDecoder.decode(
                        selectFirst(Class("loop-grid loop"))!!.attr("href"),
                        StandardCharsets.UTF_8,
                    ),
                    authors = emptyList(),
                    categories = selectFirst(Class("tags"))!!.select(Tag("span")).map { it.text() },
                    publishedDateTime = null,
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("next page-numbers")) != null
    }
}
