package com.github.keunwon.techblogscrap

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class DateTimeOptions {
    // ex) Jul 6, 2022
    MMM_ENG_DAY_COMMA_YYYY {
        private val shortDateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

        override fun convert(date: String): LocalDateTime {
            return if (date.split(" ")[1].length == 2) {
                LocalDate.parse(date, shortDateTimeFormatter).atStartOfDay()
            } else {
                LocalDate.parse(date, dateTimeFormatter).atStartOfDay()
            }
        }
    },

    EPOCH_MILLI {
        override fun convert(date: String): LocalDateTime {
            val instant = Instant.ofEpochMilli(date.toLong())
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    };

    abstract fun convert(date: String): LocalDateTime

    fun convert(date: Long): LocalDateTime = convert(date.toString())
}
