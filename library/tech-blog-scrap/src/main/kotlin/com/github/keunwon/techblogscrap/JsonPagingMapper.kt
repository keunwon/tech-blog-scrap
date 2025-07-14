package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.keunwon.techblogscrap.support.LocalDateTimeConverterType

internal class JsonPagingMapper(
    private val properties: JsonTagProperty,
    private val objectMapper: ObjectMapper,
) : PagingMapper<BlogPost> {

    override fun map(data: String): BlogPost {
        val node = objectMapper.readTree(data)
        val blogPage = BlogPage(
            pageSize = node.getValue(properties.pageSize),
            current = node.getValue(properties.page),
        )

        val contents = node.getValue<ArrayNode>(properties.jPath.contentPrefix)
            .map {
                val converterType = LocalDateTimeConverterType.getOrNull(properties.jPath.dateTimeFormat)!!
                val strDateTime = if (converterType == LocalDateTimeConverterType.TIMESTAMP) {
                    it.getValue<Long>(properties.jPath.publishedDateTime).toString()
                } else {
                    it.getValue(properties.jPath.publishedDateTime)
                }

                Content(
                    title = it.getValue(properties.jPath.title),
                    comment = it.getOrNull(properties.jPath.comment) ?: "",
                    categories = it.getList(properties.jPath.category),
                    authors = it.getList(properties.jPath.author),
                    url = it.getOrNull(properties.jPath.url) ?: "",
                    publishedDateTime = converterType.converter(strDateTime),
                )
            }

        return BlogPost(blogPage, contents)
    }

    private inline fun <reified T : Any> JsonNode.getList(tag: String): List<T> {
        val keys = tag.split(".")
        val result = mutableListOf<T>()
        var cur = this

        for ((i, key) in keys.withIndex()) {
            cur = cur.get(key) ?: return emptyList()

            if (cur is ArrayNode) {
                val nextTag = keys.drop(i + 1).joinToString("")
                result.addAll(cur.mapNotNull { it.getOrNull(nextTag) })
                break
            }
        }

        return result
    }

    private fun <T : Any> JsonNode.getValue(tag: String): T = getOrNull(tag)!!

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> JsonNode.getOrNull(tag: String): T? {
        if (tag.isBlank()) return null

        val keys = tag.split(".")
        var cur = this

        for (key in keys) {
            cur = cur.get(key) ?: return null
        }

        return when (cur) {
            is IntNode -> cur.intValue()
            is LongNode -> cur.longValue()
            is TextNode -> cur.textValue()
            else -> cur
        } as T?
    }
}
