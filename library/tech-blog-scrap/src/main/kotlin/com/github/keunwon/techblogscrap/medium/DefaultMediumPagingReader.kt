package com.github.keunwon.techblogscrap.medium

import com.github.keunwon.techblogscrap.BlogPost
import com.github.keunwon.techblogscrap.DateTimeOption
import com.github.keunwon.techblogscrap.NoOffsetPagingReader
import com.github.keunwon.techblogscrap.findXPath
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

class DefaultMediumPagingReader(
    override val driver: RemoteWebDriver,
    override val url: String,
    override val name: String,
) : NoOffsetPagingReader<BlogPost>() {
    override val contentsXPath: String = "/html/body/div[1]/div/div[3]/div[2]/div[2]/div/main/div/div[2]/div/div/div"

    override fun conditionElement(element: WebElement): Boolean {
        return element.getAttribute("class") == "ac ck"
    }

    override fun tryToObj(element: WebElement): Result<BlogPost> {
        return runCatching {
            BlogPost(
                title = element.findXPath(POST_TITLE_XPATH).text,
                comment = element.findXPath(POST_COMMENT_XPATH).text,
                authors = listOf(),
                publishedDateTime = DateTimeOption.MMM_ENG_DAY_COMMA_YYYY
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
