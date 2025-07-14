package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.annotation.JsonUnwrapped

internal data class JsonTagProperty(
    val name: String,
    val firstUrl: String,
    val remainingUrlTemplate: String,
    val pageSize: String,
    val page: String,
    val totalPage: String,

    @get:JsonUnwrapped
    var jPath: JPath = JPath.EMPTY,
)

internal data class JsoupTagProperty(
    val name: String,
    val firstUrl: String,
    val remainingUrlTemplate: String,
    val pageSize: Int,
    val page: Int,

    @get:JsonUnwrapped
    var xPath: XPath = XPath.EMPTY,
)

internal data class JsoupAndJsonProperty(
    val name: String,
    val pageSize: Int,
    val page: Int,

    @get:JsonUnwrapped
    var xPath: XPath = XPath.EMPTY,

    @get:JsonUnwrapped
    var jPath: JPath = JPath.EMPTY,
)

internal data class JPath(
    val contentPrefix: String,
    val title: String,
    val comment: String,
    val url: String,
    val category: String,
    val author: String,
    val dateTimeFormat: String,
    val publishedDateTime: String,
) {
    companion object {
        val EMPTY = JPath(
            contentPrefix = "",
            title = "",
            comment = "",
            url = "",
            category = "",
            author = "",
            dateTimeFormat = "",
            publishedDateTime = "",
        )
    }
}

internal data class XPath(
    val content: String,
    val title: String,
    val comment: String,
    val url: String,
    val categorySeparator: String = ",",
    val category: String,
    val authorSeparator: String = ",",
    val author: String,
    val dateTimeFormat: String,
    val publishedDateTime: String,
) {
    companion object {
        val EMPTY = XPath(
            content = "",
            title = "",
            comment = "",
            url = "",
            categorySeparator = "",
            category = "",
            authorSeparator = "",
            author = "",
            dateTimeFormat = "",
            publishedDateTime = "",
        )
    }
}
