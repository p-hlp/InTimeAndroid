package com.example.intimesimple

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
object LiveDataTestUtil {
    @Throws(InterruptedException::class)
    fun <T> awaitValue(liveData: LiveData<T>): T {
        val latch = CountDownLatch(1)
        var data: T? = null
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                liveData.removeObserver(this)
                latch.countDown()
            }
        }
        ArchTaskExecutor.getMainThreadExecutor().execute {
            liveData.observeForever(observer)
        }
        latch.await(10, TimeUnit.SECONDS)
        return data!!
    }
}