package com.github.keunwon.techblogscrap

import com.github.keunwon.techblogscrap.support.LocalDateTimeConverterType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class JsoupPagingMapper(
    private val properties: JsoupTagProperty,
) : PagingMapper<List<Content>> {

    override fun map(data: String): List<Content> {
        val document = Jsoup.parse(data)
        val element = document.selectXpath(properties.xPath.content)

        return element.map { el ->
            properties.xPath.run {
                Content(
                    title = el.getValue(title),
                    comment = el.getOrNull(comment) ?: "",
                    categories = el.getOrNull(category)?.split(authorSeparator) ?: emptyList(),
                    authors = el.getOrNull(author)?.split(authorSeparator) ?: emptyList(),
                    url = el.getUrl(url),
                    publishedDateTime = el.getOrNull(properties.xPath.publishedDateTime)?.let { strDateTime ->
                        LocalDateTimeConverterType.convert(properties.xPath.dateTimeFormat, strDateTime)
                    }
                )
            }
        }
    }

    private fun Element.getValue(xPath: String): String = getOrNull(xPath)!!

    private fun Element.getOrNull(xPath: String): String? {
        if (xPath.isBlank()) return null
        return selectXpath(xPath).text()
    }

    private fun Element.getUrl(tag: String): String {
        val arr = tag.replace("}}", "").split("{{")

        return if (arr.size == 1) {
            selectXpath(arr[0])[0].attr("href")
        } else {
            arr[0] + selectXpath(arr[1])[0].attr("href")
        }
    }
}
