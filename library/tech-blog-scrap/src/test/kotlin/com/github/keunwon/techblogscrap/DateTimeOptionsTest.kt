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

        context("EPOCH_MILLI") {
            test("1688352277664") {
                val date = "1688352277664"
                val actual = DateTimeOptions.EPOCH_MILLI.convert(date)
                println(actual)
            }
        }


        context("INSTANT") {
            test("ISO-8601") {
                val date = "2014-10-06T15:00:00.000Z"
                shouldNotThrowExactly<Exception> { DateTimeOptions.INSTANT.convert(date) }
            }
        }
    }
}
