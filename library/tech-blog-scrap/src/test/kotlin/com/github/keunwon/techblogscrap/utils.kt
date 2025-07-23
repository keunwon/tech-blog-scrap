package com.github.keunwon.techblogscrap

import com.fasterxml.jackson.databind.ObjectMapper
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

internal val testObjectMapper = ObjectMapper()

internal val headlessOptions = ChromeOptions().apply {
    addArguments(
        "--headless=new",
        "--disable-gpu",
        "--no-sandbox",
        "--window-size=1920,1080",
        "--disable-images",
        "--blink-settings=imagesEnabled=false",
        "user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36",
    )
}

internal val options = ChromeOptions().apply {
    addArguments(
        "--disable-gpu",
        "--no-sandbox",
        "--window-size=1920,1080",
        "--disable-images",
        "--blink-settings=imagesEnabled=false",
        "user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36",
    )
}

internal fun generateHeadlessDriver() = ChromeDriver(headlessOptions)

internal fun generateDriver() = ChromeDriver(options)
