package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class WanteddevJsopPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://wanteddev.github.io/"
        } else {
            "https://wanteddev.github.io/blog/page${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/div/div/div[2]/div").map { element ->
            element.run {
                val arr = selectFirst(Class("meta"))!!.text()
                    .split("] ")
                    .last()
                    .split(" - ")

                BlogPost(
                    title = selectFirst(Class("post-link"))!!.text(),
                    comment = selectFirst(Class("excerpt"))!!.text(),
                    url = "https://wanteddev.github.io${selectFirst(Class("post-link"))!!.attr("href")}",
                    authors = if (arr.size == 2) listOf(arr[1]) else emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY.convert(arr[0])
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pagination"))!!.select(Tag("a")).last()!!.text() == "Next"
    }
}
