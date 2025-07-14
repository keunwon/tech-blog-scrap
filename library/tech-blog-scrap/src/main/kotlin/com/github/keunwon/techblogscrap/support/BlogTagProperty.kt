package com.github.keunwon.techblogscrap.support

import com.github.keunwon.techblogscrap.JsonTagProperty
import com.github.keunwon.techblogscrap.JsoupTagProperty

internal data class BlogTagProperty(
    val json: List<JsonTagProperty>,
    val jsoup: List<JsoupTagProperty>,
)
