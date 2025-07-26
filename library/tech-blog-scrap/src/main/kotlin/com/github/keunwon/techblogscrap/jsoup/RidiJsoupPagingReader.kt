package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class RidiJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://ridicorp.com/story-category/tech-blog/"
        } else {
            "https://ridicorp.com/story-category/tech-blog/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectXpath("/html/body/div[2]/main/div/ul/li")
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = selectFirst(Class("entry-title"))!!.text(),
                    comment = selectFirst(Class("entry-meta"))!!.selectFirst(Tag("p"))!!.text(),
                    url = selectFirst(Class("entry-title"))!!.selectFirst(Tag("a"))!!.attr("href"),
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = null,
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("next page-numbers")) != null
    }
}
