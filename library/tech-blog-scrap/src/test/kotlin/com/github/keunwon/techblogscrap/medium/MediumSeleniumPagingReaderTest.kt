package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.NoOffsetPagingSeleniumReader
import com.github.keunwon.techblogscrap.findXPath
import com.github.keunwon.techblogscrap.generateDriver
import com.github.keunwon.techblogscrap.generateHeadlessDriver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

class MediumSeleniumPagingReaderTest : FunSpec() {
    init {
        test("왓챠 블로그 글 읽기") {
            val watchaReader = generateMediumReader(
                url = "https://medium.com/@watcha",
                name = "왓챠",
            )

            val actual = watchaReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual[0].title.shouldNotBeBlank()
            actual[0].comment.shouldNotBeBlank()
            actual[0].publishedDateTime.shouldNotBeNull()
        }

        test("무신사 블로그 글 읽기") {
            val musinsaReader = generateMediumReader(
                url = "https://medium.com/@musinsa-tech",
                name = "무신사",
            )

            val actual = musinsaReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual[0].title.shouldNotBeBlank()
            actual[0].comment.shouldNotBeBlank()
            actual[0].publishedDateTime.shouldNotBeNull()
        }
    }

    private fun generateMediumBrowserReader(url: String, name: String) =
        MediumSeleniumPagingReader(generateDriver(), url, name)

    private fun generateMediumReader(url: String, name: String) =
        MediumSeleniumPagingReader(generateHeadlessDriver(), url, name)
}

internal class MediumSeleniumPagingReader(
    override val driver: RemoteWebDriver,
    override val url: String,
    override val name: String,
) : NoOffsetPagingSeleniumReader<BlogPost>() {
    override val contentsXPath: String = "/html/body/div[1]/div/div[3]/div[2]/div[2]/div/main/div/div[2]/div/div/div"

    override fun conditionElement(element: WebElement): Boolean {
        return element.getAttribute("class") == "ac ck"
    }

    override fun tryToObj(element: WebElement): Result<BlogPost> {
        return runCatching {
            BlogPost(
                title = element.findXPath(POST_TITLE_XPATH).text,
                comment = element.findXPath(POST_COMMENT_XPATH).text,
                url = "",
                authors = listOf(),
                publishedDateTime = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY
                    .convert(element.findXPath(POST_PUBLISHED_DATE_TIME_XPATH).text),
            )
        }
    }

    companion object {
        private const val POST_TITLE_XPATH = "div/div/article/div/div/div/div/div[1]/div/div[2]/div[1]/div[1]/a/h2"

        private const val POST_COMMENT_XPATH =
            "div/div/article/div/div/div/div/div[1]/div/div[2]/div[1]/div[1]/a/div/h3"

        private const val POST_PUBLISHED_DATE_TIME_XPATH =
            "div/div/article/div/div/div/div/div[1]/div/div[2]/div[1]/div[2]/div/span/div/div[1]/span"
    }
}
