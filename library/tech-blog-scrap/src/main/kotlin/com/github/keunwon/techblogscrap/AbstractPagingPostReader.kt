package com.github.keunwon.techblogscrap

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.locks.ReentrantLock

internal abstract class AbstractPagingPostReader<T> : PostRead<T> {
    protected var pageSize = 10

    @Volatile
    protected var current = 0

    @Volatile
    protected var page = 0

    @Volatile
    protected var results = CopyOnWriteArrayList<T>()

    private val lock = ReentrantLock()

    override fun readOrNull(): T? {
        this.lock.lock()

        try {
            if (results.isEmpty() || current >= pageSize) {
                doReadPage()
                ++page

                if (current >= pageSize) {
                    current = 0
                }
            }

            val next = current++
            return if (next < results.size) results[next] else null
        } finally {
            this.lock.unlock()
        }
    }

    protected abstract fun doReadPage()
}
