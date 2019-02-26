package com.bulygin.nikita.healthapp.data

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentLinkedQueue

open class BaseConsumer<T>(val dao: BaseDao<T>, private val backgroundScheduler: Scheduler) {
    private val container = ConcurrentLinkedQueue<T>()
    private var lastScheduled: Disposable? = null

    open fun onNewItem(newItem: T) {
        container.offer(newItem)
        startInsertIfNeed()
    }

    private fun startInsertIfNeed() {
        if (lastScheduled == null || lastScheduled?.isDisposed!!) {
            lastScheduled = backgroundScheduler.createWorker().schedule {
                do {
                    val item = container.poll()
                    item?.let { dao.insert(item) }
                } while (item != null)
            }
        }
    }


}
