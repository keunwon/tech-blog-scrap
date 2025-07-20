package com.github.keunwon.techblogscrap

import java.nio.file.Files
import org.openqa.selenium.By.xpath
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import kotlin.io.path.Path

fun RemoteWebDriver.scrollDown() {
    executeScript("""window.scrollTo(0, document.body.scrollHeight)""")
    executeScript("""window.scrollTo(0, document.body.scrollHeight-50);""")
}

fun RemoteWebDriver.isScrollEnd(): Boolean {
    return executeScript("return window.innerHeight + window.scrollY >= document.body.offsetHeight;") as Boolean
}

fun RemoteWebDriver.saveScreenshot(filename: String = "") {
    val file = getScreenshotAs(OutputType.FILE)
    val targetName = if (filename.isNotBlank()) "${filename}.png" else file.name
    Files.copy(file.inputStream(), Path("/Users/root/Downloads/$targetName"))
}

fun WebDriver.findXPath(xPath: String): WebElement = findElement(xpath(xPath))

fun WebDriver.findAllXPath(xPath: String): List<WebElement> = findElements(xpath(xPath))

fun WebElement.findXPath(xPath: String): WebElement = findElement(xpath(xPath))

fun WebElement.findAllXPath(xpathExpression: String): List<WebElement> = findElements(xpath(xpathExpression))
