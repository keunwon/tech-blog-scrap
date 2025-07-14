package com.github.keunwon.techblogscrap.support

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal enum class LocalDateTimeConverterType(
    val title: String,
    val converter: (String) -> LocalDateTime,
) {
    YYYY_MM_DD_COMMA(
        "yyyy.MM.dd",
        { date ->
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            LocalDate.parse(resolveDate(date).take(10), formatter).atStartOfDay()
        }
    ),

    YYYY_MM_DD_DASH(
        "yyyy-MM-dd",
        { date ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(resolveDate(date).take(10), formatter).atStartOfDay()
        }
    ),

    TIMESTAMP(
        "timestamp",
        { date ->
            val instant = Instant.ofEpochSecond(date.toLong())
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        }
    ),

    YYYY_MM_DD_HH_MM_SS(
        "yyyy-MM-dd HH-mm-ss",
        { date ->
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            LocalDateTime.parse(date, formatter)
        }
    ),

    YYYY_MM_DD_HH_MM_SS_DASH(
        "yyyy-MM-dd HH:mm:ss",
        { date ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime.parse(date.take(19), formatter)
        }
    );

    companion object {
        private val RESOLVE_DATE_REGEX = """[^0-9._:-]""".toRegex()

        fun getOrNull(formatName: String): LocalDateTimeConverterType? = entries.find { it.title == formatName }

        fun convert(format: String, strDateTime: String): LocalDateTime {
            return getOrNull(format)?.converter(strDateTime) ?: run {
                val dateFormatter = DateTimeFormatter.ofPattern(format)
                LocalDateTime.parse(strDateTime, dateFormatter)
            }
        }

        private fun resolveDate(date: String): String {
            return date.replace(RESOLVE_DATE_REGEX, "").trim { !it.isDigit() }
        }
    }
}
