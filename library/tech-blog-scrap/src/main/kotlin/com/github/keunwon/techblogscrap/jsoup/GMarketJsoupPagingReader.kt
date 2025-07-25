package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Id
import org.jsoup.select.Evaluator.Tag

class GMarketJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://dev.gmarket.com/?page=${page + 1}"

    override fun convert(document: Document): List<BlogPost> {
        val contents = document.selectFirst(Id("cMain"))!!.select(Id("mArticle"))
        return contents.map { content ->
            content.run {
                BlogPost(
                    title = selectFirst(Class("tit_post"))!!.text(),
                    comment = selectFirst(Class("txt_post"))!!.text(),
                    url = "https://dev.gmarket.com${selectFirst(Class("link_post"))!!.attr("href")}",
                    authors = emptyList(),
                    categories = listOf(selectFirst(Class("link_cate"))!!.text()),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA.convert(extraReqDate()),
                )
            }
        }
    }

    override fun doNext(document: Document) {
        return
    }

    override fun doHasNetPage(document: Document): Boolean {
        val last = document.selectFirst(Class("inner_paging"))!!.select(Tag("a")).last()!!
        return last.attr("href").isNotBlank()
    }

    private fun Element.extraReqDate(): String {
        val prefix = "var repDate = '"
        val date = selectFirst(Class("detail_info"))!!.getElementsByTag("script").toString().let {
            val startIndex = it.indexOf(prefix)
            val endIndex = it.indexOf("';")
            it.substring(startIndex + prefix.length, endIndex)
        }
        return date.replace(" ", "").split(".").take(3).joinToString(".")
    }
}
