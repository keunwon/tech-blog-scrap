package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOption
import com.github.keunwon.techblogscrap.NoOffsetPagingReader
import com.github.keunwon.techblogscrap.findXPath
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

class YogiyoMediumReader(
    override val driver: RemoteWebDriver,
) : NoOffsetPagingReader<BlogPost>() {
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
                authors = listOf(element.findXPath(AUTHOR_XPATH).text),
                publishedDateTime = DateTimeOption.MMM_ENG_DAY_COMMA_YYYY
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
