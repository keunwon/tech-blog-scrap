package com.github.keunwon.techblogscrap

import io.kotest.assertions.throwables.shouldNotThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class DateTimeOptionsTest : FunSpec() {
    init {
        context("MMM_ENG_DAY_COMMA_YYYY") {
            test("Jul 6, 2022") {
                val date = "Jul 6, 2022"
                val actual = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY.convert(date)
                actual shouldBe LocalDateTime.of(2022, 7, 6, 0, 0, 0)
            }

            test("Jul 06, 2022") {
                val date = "Jul 06, 2022"
                val actual = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY.convert(date)
                actual shouldBe LocalDateTime.of(2022, 7, 6, 0, 0, 0)
            }

        }

        context("MMMM_ENG_DAY_COMMA_YYYY") {
            test("July 9, 2025") {
                val date = "July 9, 2025"
                val actual = DateTimeOptions.MMMM_ENG_DAY_COMMA_YYYY.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 9, 0, 0, 0)
            }
        }

        context("DAY_MMM_ENG_YYYY") {
            test("10 Feb 2025") {
                val date = "10 Feb 2025"
                val actual = DateTimeOptions.DAY_MMM_ENG_YYYY.convert(date)
                actual shouldBe LocalDateTime.of(2025, 2, 10, 0, 0, 0)
            }
        }

        context("INSTANT") {
            test("ISO-8601") {
                val date = "2014-10-06T15:00:00.000Z"
                shouldNotThrowExactly<Exception> { DateTimeOptions.INSTANT.convert(date) }
            }
        }

        context("EPOCH_MILLI") {
            test("1688352277664") {
                val date = "1688352277664"
                val actual = DateTimeOptions.EPOCH_MILLI.convert(date)
                println(actual)
            }
        }

        context("YYYY_MM_DD_COMMA") {
            test("2025.07.01") {
                val date = "2025.07.01"
                val actual = DateTimeOptions.YYYY_MM_DD_COMMA.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 1, 0, 0, 0)
            }

            test("2025.7.1") {
                val date = "2025.7.1"
                val actual = DateTimeOptions.YYYY_MM_DD_COMMA.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 1, 0, 0, 0)
            }

            test("2025.7.20") {
                val date = "2025.7.20"
                val actual = DateTimeOptions.YYYY_MM_DD_COMMA.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 20, 0, 0, 0)
            }

            test("2025.11.2") {
                val date = "2025.11.2"
                val actual = DateTimeOptions.YYYY_MM_DD_COMMA.convert(date)
                actual shouldBe LocalDateTime.of(2025, 11, 2, 0, 0, 0)
            }
        }

        context("YYYY_MM_DD_COMMA_T_HH_MM_SS_COLON") {
            test("2025.07.24T08:42:00") {
                val date = "2025.07.24T08:42:00"
                val actual = DateTimeOptions.YYYY_MM_DD_COMMA_T_HH_MM_SS_COLON.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 24, 8, 42, 0)
            }
        }

        context("YYYY_MM_DD_DASH_T_HH_MM_SS_COLON") {
            test("2025-07-18T16:34:30") {
                val date = "2025-07-18T16:34:30"
                val actual = DateTimeOptions.YYYY_MM_DD_DASH_T_HH_MM_SS_COLON.convert(date)
                actual shouldBe LocalDateTime.of(2025, 7, 18, 16, 34, 30)
            }
        }
    }
}
