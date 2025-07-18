package com.github.keunwon.techblogscrap

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

internal class FilePostTemplate : PostTemplate {
    override fun fetch(query: String): String {
        val file = File(query)
        return BufferedReader(FileReader(file)).use { it.readText() }
    }
}
