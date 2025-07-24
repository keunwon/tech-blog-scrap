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

    // ex) 10 Feb 2025
    DAY_MMM_ENG_YYYY {
        private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)

        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, formatter).atStartOfDay()
        }
    },

    INSTANT {
        override fun convert(date: String): LocalDateTime {
            val instant = Instant.parse(date)
            return LocalDateTime.ofInstant(instant, systemZoneId)
        }
    },

    EPOCH_MILLI {
        override fun convert(date: String): LocalDateTime {
            val instant = Instant.ofEpochMilli(date.toLong())
            return LocalDateTime.ofInstant(instant, systemZoneId);
        }
    },

    YYYY_YY_DD_ALL_COMMA {
        private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.")

        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, formatter).atStartOfDay()
        }
    },

    YYYY_MM_DD_DASH {
        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE).atStartOfDay()
        }
    };


    abstract fun convert(date: String): LocalDateTime

    fun convert(date: Long): LocalDateTime = convert(date.toString())

    companion object {
        private val systemZoneId = ZoneId.systemDefault()
    }
}
