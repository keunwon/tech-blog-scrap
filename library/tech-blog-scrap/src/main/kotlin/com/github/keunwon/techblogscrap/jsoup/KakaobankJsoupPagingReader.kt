package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class KakaobankJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://tech.kakaobank.com/"
        } else {
            "https://tech.kakaobank.com/page/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        val posts = document.selectFirst(Class("col-12 col-md-8 float-left content"))!!.select(Class("post"))
        return posts.map { post ->
            post.run {
                BlogPost(
                    title = selectFirst(Class("post-title"))!!.text(),
                    comment = selectFirst(Class("post-summary"))!!.text(),
                    url = selectFirst(Class("post-title"))!!
                        .selectFirst(Tag("a"))!!
                        .attr("href")
                        .replace("../..", "https://tech.kakaobank.com"),
                    authors = select(Class("author")).map { it.text().replace(" ", "") },
                    categories = selectFirst(Class("sidebar-tags"))!!.select(Tag("span")).map { it.text() },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_DASH.convert(selectFirst(Class("date"))!!.text()),
                )
            }
        }
    }

    override fun doNext(document: Document) {
        return
    }

    override fun doHasNetPage(document: Document): Boolean {
        return document.selectFirst(Class("pag-next")) != null
    }
}
