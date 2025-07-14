package com.github.keunwon.techblogscrap

import java.time.LocalDateTime

internal data class BlogPost(
    val page: BlogPage,
    val contents: List<Content>,
)

internal data class BlogPage(
    val pageSize: Int,
    val current: Int,
)

internal data class Content(
    val title: String,
    val comment: String,
    val categories: List<String>,
    val authors: List<String>,
    val url: String,
    val publishedDateTime: LocalDateTime?,
)
