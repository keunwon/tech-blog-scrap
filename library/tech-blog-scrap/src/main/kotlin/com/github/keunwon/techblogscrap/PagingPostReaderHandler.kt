package com.github.keunwon.techblogscrap

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Runnable

internal class PagingPostReaderHandler {
    private val queue: BlockingQueue<Runnable> = LinkedBlockingQueue(10)
    private val executor = ThreadPoolExecutor(5, 40, 60L, TimeUnit.SECONDS, queue)

    fun <T> handle(postRead: PostRead<T>) {
        executor.submit {
            try {
                val items = mutableListOf<T>()
                while (true) {
                    val item = postRead.readOrNull() ?: break
                    items.add(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }
}
