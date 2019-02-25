package com.bulygin.nikita.healthapp.data

import com.bulygin.nikita.healthapp.ui.TextListener
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentLinkedQueue

class SimpleTypingErrorsConsumer(private val typeErrorDao: TypingErrorsDao,
                                 private val backgroundScheduler: Scheduler) : TextListener.TypingErrorConsumer {

    private val errorsContainer = ConcurrentLinkedQueue<TypingErrorsEntity>()
    private var lastScheduled: Disposable? = null

    override fun onWordTypingEnd(s: String, eraseCount: Int) {
        if (eraseCount == 0) {
            return
        }
        val time = getCurrentTime()
        errorsContainer.offer(TypingErrorsEntity(null, time, eraseCount))
        startInsertIfNeed()
    }

    private fun startInsertIfNeed() {
        if (lastScheduled == null || lastScheduled?.isDisposed!!) {
            lastScheduled = backgroundScheduler.createWorker().schedule {
                do {
                    val item = errorsContainer.poll()
                    item?.let { typeErrorDao.insert(item) }
                } while (item != null)
            }
        }
    }

    private fun getCurrentTime(): Long = System.currentTimeMillis()

}

