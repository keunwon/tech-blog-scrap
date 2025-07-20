package com.github.keunwon.techblogscrap

interface PostConverter<T, U> {
    fun convert(dat: T): U?
}
