package com.github.keunwon.techblogscrap

interface Pageable {
    // 다음 페이지로 이동
    fun next()

    // 다음 페이지가 존재하는지 확인
    fun hasNextPage(): Boolean
}
