package com.github.keunwon.techblogscrap.support

import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

internal object BlogTagYamlParser {
    fun load(file: File): BlogTagProperty {
        return objectMapper.readValue(file.inputStream())
    }
}
