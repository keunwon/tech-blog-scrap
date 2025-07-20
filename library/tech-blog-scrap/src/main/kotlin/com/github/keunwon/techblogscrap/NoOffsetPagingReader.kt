package com.github.keunwon.techblogscrap

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory

abstract class NoOffsetPagingReader<T> : PostReader<T>, PostReaderStream, Pageable {
    protected val logger = LoggerFactory.getLogger(this::class.java)

    abstract val driver: RemoteWebDriver
    abstract val url: String
    abstract val name: String
    abstract val contentsXPath: String

    var initialized = false
    var current = 0
    var page = 1
    var results = listOf<T>()

    override fun read(): T? {
        if (!initialized) {
            while (hasNextPage()) {
                logger.info("next page $page")
                ++page
                next()
            }

            results = getContents(driver.findAllXPath(contentsXPath))
            initialized = true
        }

        val next = current++
        return if (next < results.size) results[next] else null
    }

    private fun getContents(elements: List<WebElement>): List<T> {
        val contents = mutableListOf<T>()
        var isError = false

        for ((i, element) in elements.withIndex()) {
            if (conditionElement(element)) {
                tryToObj(element)
                    .onSuccess { contents.add(it) }
                    .onFailure { e ->
                        if (e is NoSuchElementException) {
                            logger.info("${i + 1}번째 글의 element 찾지 못하여 추가하지 않습니다.")
                        } else {
                            logger.warn("${i + 1}번째 글 파싱에 실패아여 추가하지 않습니다.", e)
                        }
                        isError = true
                    }
            }
        }

        if (isError) driver.saveScreenshot("${LocalDateTime.now().format(NOW_DATE_TIME_FORMATTER)}_$name")

        return contents
    }

    abstract fun conditionElement(element: WebElement): Boolean

    abstract fun tryToObj(element: WebElement): Result<T>

    override fun open() {
        logger.info("$url 접속합니다.")
        driver.get(url)
    }

    override fun close() {
        driver.quit()
    }

    override fun next() {
        val before = driver.findAllXPath(contentsXPath).size

        driver.scrollDown()
        repeat(10) {
            TimeUnit.MILLISECONDS.sleep(300L)
            val now = driver.findAllXPath(contentsXPath).size
            if (before < now) return@repeat
        }
    }

    override fun hasNextPage(): Boolean = !driver.isScrollEnd()

    companion object {
        private val NOW_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    }
}
