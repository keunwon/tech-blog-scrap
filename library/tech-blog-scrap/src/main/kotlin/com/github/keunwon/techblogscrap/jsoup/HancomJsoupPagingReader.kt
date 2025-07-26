package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class HancomJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String {
        return if (!initialized) {
            "https://tech.hancom.com/blog/"
        } else {
            "https://tech.hancom.com/blog/${page + 1}/"
        }
    }

    override fun convert(document: Document): List<BlogPost> {
        return document
            .selectXpath("/html/body/div[1]/div/div/div/main/article/div/div/section[2]/div/div/div/div[2]/div/div[1]/div")
            .map { element ->
                element.run {
                    BlogPost(
                        title = selectFirst(Class("uc_post_list_title"))!!.selectFirst(Tag("a"))!!.text(),
                        comment = selectFirst(Class("uc_post_content"))!!.text(),
                        url = selectFirst(Class("uc_post_list_title"))!!.selectFirst(Tag("a"))!!.attr("href"),
                        authors = selectFirst(Class("ue-meta-data"))!!
                            .selectFirst(Tag("span"))!!.text().replace(" ", "").split(", "),
                        categories = listOf(),
                        publishedDateTime = DateTimeOptions.YYYY_MM_DD_KO
                            .convert(selectFirst(Class("ue-grid-item-meta-data"))!!.text()),
                    )
                }
            }
    }

    override fun doHasNetPage(document: Document): Boolean {
        val pagination = document.getPagination().children()
        return pagination.last()!!.tagName() != "span"
    }

    private fun Document.getPagination(): Element {
        return selectFirst(Class("archive_pagination uc-filter-pagination"))!!
    }
}
