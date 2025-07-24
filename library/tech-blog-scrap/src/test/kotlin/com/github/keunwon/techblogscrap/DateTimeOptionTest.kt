package com.github.keunwon.techblogscrap

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime


class DateTimeOptionTest : FunSpec() {
    init {
        context("MMM_ENG_DAY_COMMA_YYYY 형식으로 변환") {
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

        context("instance") {
            test("1688352277664") {
                val date = "1688352277664"
                val actual = DateTimeOptions.EPOCH_MILLI.convert(date)
                println(actual)
            }
        }
    }
}
