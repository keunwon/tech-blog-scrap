package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class DoctorChaJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://blog.doctor-cha.com/engineering"

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("saas-article")).map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("article-title"))!!.text(),
                    comment = selectFirst(Class("article-excerpt"))!!.text(),
                    url = "https://blog.doctor-cha.com${selectFirst(Class("article-cover"))!!.attr("href")}",
                    authors = emptyList(),
                    categories = select(Class("article-tag")).map { it.selectFirst(Tag("span"))!!.text() },
                    publishedDateTime = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY
                        .convert(selectFirst(Class("article-publish-date"))!!.selectFirst(Tag("time"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
