package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class DevoceanJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String =
        "https://devocean.sk.com/blog/index.do?ID=&boardType=&searchData=&searchDataMain=&page=${page + 1}&subIndex=&searchText=&techType=&searchDataSub=&comment=&p=BLOG"

    override fun convert(document: Document): List<BlogPost> {
        return document.select(Class("sec-cont")).map { element ->
            element.run {
                val id = selectFirst(Class("title"))!!.attr("onclick").split("'")[1]
                BlogPost(
                    title = selectFirst(Class("title"))!!.text(),
                    comment = selectFirst(Class("desc"))?.text() ?: "",
                    url = "https://devocean.sk.com/blog/techBoardDetail.do?ID=$id&boardType=techBlog&searchData=&searchDataMain=&page=&subIndex=&searchText=&techType=&searchDataSub=&comment=&p=BLOG",
                    authors = listOf(selectFirst(Class("author"))!!.selectFirst(Tag("em"))!!.text()),
                    categories = selectFirst(Class("tag"))!!.select(Tag("span")).map { it.text().replace("#", "") },
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA.convert("20" + selectFirst(Class("date"))!!.text()),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean {
        val pagination = document.selectFirst(Class("sec-area-paging")) ?: return true // 137P 화면 오류로 목록이 2개 보임(정상 11개)
        val nextPage = pagination.select(Tag("a")).last()!!.attr("onclick").split(")")[0].replace("goPage(", "").toInt()
        val current = pagination.selectFirst(Class("active"))!!.text().toInt()

        return current < nextPage
    }
}
