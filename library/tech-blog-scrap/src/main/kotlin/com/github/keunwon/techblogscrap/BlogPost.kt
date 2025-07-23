package com.github.keunwon.techblogscrap

import java.time.LocalDateTime

data class BlogPost(
    val title: String,
    val comment: String,
    val url: String,
    val authors: List<String>,
    val publishedDateTime: LocalDateTime?,
)
