package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.jsoup.select.Evaluator

class KakaostyleJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    private var path = "/ko"

    override fun getRequestUrl(): String = "https://devblog.kakaostyle.com${path}"

    override fun convert(document: Document): List<BlogPost> {
        val elements = document.selectXpath("/html/body/div[3]/div/div[1]/div").drop(1)
        return elements.map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Evaluator.Class("posts-title"))!!.getElementsByTag("a")[0].text(),
                    comment = selectFirst(Evaluator.Class("card-body"))?.selectFirst(Evaluator.Tag("p"))?.text() ?: "",
                    url = selectFirst(Evaluator.Class("posts-title"))!!.getElementsByTag("a")[0].attr("href"),
                    authors = selectFirst(Evaluator.Class("posts-author"))!!.text().split(" "),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.DAY_MMM_ENG_YYYY
                        .convert(selectFirst(Evaluator.Class("posts-date"))!!.text().take(11))
                )
            }
        }
    }

    override fun doNext(document: Document) {
        val page = document.getPaginate().find { it.childrenSize() == 0 }!!.text().toInt()
        path = "/ko/page/${page + 1}/"
    }

    override fun doHasNetPage(document: Document): Boolean {
        val paginate = document.getPaginate()
        val findIndex = paginate.indexOfFirst { it.childrenSize() == 0 }
        return findIndex != -1 && findIndex < paginate.lastIndex
    }

    private fun Document.getPaginate(): Elements {
        return selectFirst(Evaluator.Class("paginate-style"))!!.select(Evaluator.Tag("li"))
    }
}
