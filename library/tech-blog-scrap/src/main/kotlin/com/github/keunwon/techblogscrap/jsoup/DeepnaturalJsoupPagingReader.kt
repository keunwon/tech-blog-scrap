package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.JsoupPagingReader
import org.jsoup.nodes.Document
import org.jsoup.select.Evaluator.AttributeWithValue
import org.jsoup.select.Evaluator.Class
import org.jsoup.select.Evaluator.Tag

class DeepnaturalJsoupPagingReader : JsoupPagingReader<BlogPost>() {
    override fun getRequestUrl(): String = "https://deepnatural.ai/ko/blog"

    override fun convert(document: Document): List<BlogPost> {
        return document.selectFirst(Class("framer-y23qfb"))!!.select(AttributeWithValue("data-framer-name", "Post")).map { element ->
            element.run {
                val framerTexts = select(Class("framer-text"))
                BlogPost(
                    title = selectFirst(Tag("h3"))!!.text(),
                    comment = framerTexts[3].text(),
                    url = "https://deepnatural.ai/ko${element.attr("href").dropWhile { it == '.' }}",
                    authors = listOf(framerTexts[7].text()),
                    categories = emptyList(),
                    publishedDateTime = DateTimeOptions.YYYY_MM_DD_COMMA.convert(framerTexts[6].text().dropLast(1).replace(" ", "")),
                )
            }
        }
    }

    override fun doHasNetPage(document: Document): Boolean = false
}
