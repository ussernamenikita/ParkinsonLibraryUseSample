package com.bulygin.nikita.healthapp.data

import com.bulygin.nikita.healthapp.ui.MissClickEventsConsumer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentLinkedQueue

class SimpleMissClickConsumer(private val missClickDao: MissClickDao,
                              private val backgroundScheduler: Scheduler) : MissClickEventsConsumer {

    private val missClickContainer = ConcurrentLinkedQueue<MissClickEntity>()
    private var lastScheduled: Disposable? = null

    override fun onConsume(timestamp: Long, distance: Double, missClickCount: Int) {
        missClickContainer.offer(MissClickEntity(null, timestamp, distance))
        startInsertIfNeed()
    }

    private fun startInsertIfNeed() {
        if (lastScheduled == null || lastScheduled?.isDisposed!!) {
            lastScheduled = backgroundScheduler.createWorker().schedule {
                do {
                    val item = missClickContainer.poll()
                    item?.let { missClickDao.insert(item) }
                } while (item != null)
            }
        }
    }


    private fun getCurrentTime(): Long = System.currentTimeMillis()

}