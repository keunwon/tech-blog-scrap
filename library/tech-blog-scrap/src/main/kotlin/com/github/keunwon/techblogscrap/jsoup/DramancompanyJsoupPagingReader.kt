package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.Class

class DramancompanyJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://dramancompany.github.io/tech-blog/"

    override fun convert(document: Document): List<BlogPost> {
        return document.selectXpath("/html/body/main/div/div/ul/li").map { element ->
            element.run {
                BlogPost(
                    title = selectFirst(Class("post-link"))!!.text(),
                    comment = "",
                    url = "https://dramancompany.github.io${selectFirst(Class("post-link"))!!.attr("href")}",
                    authors = emptyList(),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY
                        .convert(selectFirst(Class("post-meta"))!!.text())
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
