package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.AttributeWithValue
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class SkplanetJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://techtopic.skplanet.com/"

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div/div[1]/div/main/ol/li").map { element ->
            element.run {
                val (date, names) = selectFirst(Class("post-list-item"))!!
                    .select(Tag("small"))
                    .text()
                    .replace("]", "")
                    .split(" [")

                BlogPost(
                    title = selectFirst(AttributeWithValue("itemprop", "headline"))!!.text(),
                    comment = selectFirst(AttributeWithValue("itemprop", "description"))!!.text(),
                    url = "https://techtopic.skplanet.com" +
                            selectFirst(AttributeWithValue("itemprop", "url"))!!.attr("href"),
                    authors = names.split(","),
                    categories = selectFirst(Class("tags"))!!.select(Tag("span")).map { it.text() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA.convert(date),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
