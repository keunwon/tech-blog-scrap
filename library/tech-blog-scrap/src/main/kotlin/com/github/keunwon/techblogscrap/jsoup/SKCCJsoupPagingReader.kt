package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.AttributeWithValue
import org.jsoup.select.Evaluator.Class

// SK C&C
class SKCCJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://engineering-skcc.github.io/allposts/"

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("archive__item")).map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(AttributeWithValue("rel", "permalink"))!!.text(),
                    comment = selectFirst(Class("archive__item-excerpt"))!!.text(),
                    url = "https://engineering-skcc.github.io${selectFirst(AttributeWithValue("rel", "permalink"))!!.attr("href")}",
                    authors = selectFirst(Class("archive__item-authors"))!!.text().replace("Posted by ", "").split(" "),
                    categories = select(Class("archive__item-tags")).map { it.text().replace("#", "").trim() },
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_YYYY.convert(selectFirst(Class("page__meta"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
