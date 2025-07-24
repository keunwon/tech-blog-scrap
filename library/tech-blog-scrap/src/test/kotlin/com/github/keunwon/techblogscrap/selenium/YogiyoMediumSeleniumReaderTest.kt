package com.github.keunwon.techblogscrap.selenium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOptions
import com.github.keunwon.techblogscrap.NoOffsetPagingSeleniumReader
import com.github.keunwon.techblogscrap.findXPath
import com.github.keunwon.techblogscrap.generateDriver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

class YogiyoMediumSeleniumReaderTest : FunSpec() {
    init {
        test("요기요 블로그 글 읽기") {
            val yogiyoReader = YogiyoMediumSeleniumReader(generateDriver())

            val actual = yogiyoReader.use { br ->
                br.open()
                (1..100).mapNotNull { br.read() }
            }

            actual.size shouldBeGreaterThan 10
            actual.forEach {
                it.title.shouldNotBeBlank()
                it.comment.shouldNotBeBlank()
                it.authors.isNotEmpty()
                it.publishedDateTime.shouldNotBeNull()
            }
        }
    }
}

internal class YogiyoMediumSeleniumReader(
    override val driver: RemoteWebDriver,
) : NoOffsetPagingSeleniumReader<BlogPost>() {
    override val url: String = "https://techblog.yogiyo.co.kr/latest"
    override val name: String = "요기요"
    override val contentsXPath: String = "/html/body/div[1]/div[2]/div/div[4]/div/div[1]/div/div/div/div"

    override fun conditionElement(element: WebElement): Boolean {
        val classNames = element.getAttribute("class")?.split(" ") ?: emptyList()
        return classNames.containsAll(listOf("u-paddingTop20", "u-paddingBottom25"))
    }

    override fun tryToObj(element: WebElement): Result<BlogPost> {
        return runCatching {
            BlogPost(
                title = element.findXPath(TITLE_XPATH).text,
                comment = element.findXPath(COMMENT_XPATH).text,
                url = "",
                authors = listOf(element.findXPath(AUTHOR_XPATH).text),
                publishedDateTime = DateTimeOptions.MMM_ENG_DAY_COMMA_YYYY
                    .convert(element.findXPath(PUBLISHED_DATE_TIME_XPATH).text),
            )
        }
    }

    companion object {
        private const val TITLE_XPATH = "div/div[1]/div/div/div[2]/a[1]"
        private const val COMMENT_XPATH = "div/div[2]/a/section/div[2]/div/h4"
        private const val AUTHOR_XPATH = "div/div[1]/div/div/div[2]/a[1]"
        private const val PUBLISHED_DATE_TIME_XPATH = "div/div[1]/div/div/div[2]/div/a/time"
    }
}
