package com.github.keunwon.techblogscrap

import java.io.Closeable

interface PostReaderStream : Closeable {
    fun open()
}
