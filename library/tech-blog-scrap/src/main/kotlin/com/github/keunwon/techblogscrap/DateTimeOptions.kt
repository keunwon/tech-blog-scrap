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
            return if (date.length == 11) {
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

    // ex) 2025.07.01.
    YYYY_YY_DD_ALL_COMMA {
        private val shortedFormatter = DateTimeFormatter.ofPattern("yyyy.M.dd.")
        private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.")

        override fun convert(date: String): LocalDateTime {
            return if (date.length == 10) {
                LocalDate.parse(date, shortedFormatter).atStartOfDay()
            } else {
                LocalDate.parse(date, formatter).atStartOfDay()
            }
        }
    },

    // ex) 2025-07-01
    YYYY_MM_DD_DASH {
        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE).atStartOfDay()
        }
    },

    // ex) 2024.11.13
    YYYY_MM_DD_COMMA {
        private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, formatter).atStartOfDay()
        }
    },

    // ex) 2025년 03월 10일
    YYYY_MM_DD_KO {
        private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")

        override fun convert(date: String): LocalDateTime {
            return LocalDate.parse(date, formatter).atStartOfDay()
        }
    },

    // ex) 2025.07.24T08:42:00
    YYYY_MM_DD_COMMA_T_HH_MM_SS_COLON {
        private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss")

        override fun convert(date: String): LocalDateTime {
            return LocalDateTime.parse(date, formatter)
        }
    },

    YYYY_MM_DD_DASH_T_HH_MM_SS_COLON {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        override fun convert(date: String): LocalDateTime {
            return LocalDateTime.parse(date, formatter)
        }
    };

    abstract fun convert(date: String): LocalDateTime

    fun convert(date: Long): LocalDateTime = convert(date.toString())

    companion object {
        private val systemZoneId = ZoneId.systemDefault()
    }
}
