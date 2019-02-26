package com.bulygin.nikita.healthapp.data

import com.bulygin.nikita.healthapp.ui.TextListener
import io.reactivex.Scheduler

class SimpleTypingErrorsConsumer(typeErrorDao: TypingErrorsDao,
                                 backgroundScheduler: Scheduler) : TextListener.TypingErrorConsumer,
        BaseConsumer<TypingErrorsEntity>(typeErrorDao, backgroundScheduler) {


    override fun onWordTypingEnd(s: String, eraseCount: Int) {
        if (eraseCount == 0) {
            return
        }
        val time = getCurrentTime()
        this.onNewItem(TypingErrorsEntity(null, time, eraseCount))

    }

    private fun getCurrentTime(): Long = System.currentTimeMillis()

}

