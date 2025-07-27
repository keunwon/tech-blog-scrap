package com.github.keunwon.techblogscrap.jsoup

import com.github.keunwon.techblogscrap.BlogPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class DevoceanJsoupPagingReaderTest : FunSpec() {
    init {
        test("데브션 블로그 글 읽기") {
            val reader = DevoceanJsoupPagingReader()
            val posts = generateSequence { reader.read() }.toList()

            posts.size shouldBeGreaterThan 1600
            posts.last() shouldBe BlogPost(
                title = "MySQL 오픈소스 라이선스 종류와 상용 SW에 적용하는 방법",
                comment = "오픈소스 실무상 MySQL에 대한 질의를 많이 받았기에 오픈소스 라이선스 종류와 이를 상용 SW에 적용하는 방법에 대해 정리합니다. 1. 질의 내용 - 상용 Software 개발시, Mysql을 사용하고자 합니다. Mysql 특정 버전(예: 6.3)을 포함하여 Software를 개발, 판매할 때 Mysql사에 사용에 대한 라이선스 Fee를 지불해야 되는지 여부 2. MySQL 제품군 및 라이선스 MySQL 제품군은 Community Server, Standard Edition, Enterprise Edition으로 구성되어 있습니다. (http://www.mysqlkorea.com/sub.html?mcode=shop＆scode=01＆cat1=1) MySQL은 du...",
                url = "https://devocean.sk.com/blog/techBoardDetail.do?ID=193&boardType=techBlog&searchData=&searchDataMain=&page=&subIndex=&searchText=&techType=&searchDataSub=&comment=&p=BLOG",
                authors = listOf("woody"),
                categories = listOf("License Compliance"),
                publishedDateTime = LocalDateTime.of(2018, 8, 28, 0, 0),
            )
        }
    }
}
