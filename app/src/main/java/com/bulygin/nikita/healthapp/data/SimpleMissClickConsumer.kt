package com.bulygin.nikita.healthapp.data

import com.bulygin.nikita.healthapp.data.db.MissClickDao
import com.bulygin.nikita.healthapp.data.db.MissClickEntity
import com.bulygin.nikita.healthapp.ui.MissClickEventsConsumer
import io.reactivex.Scheduler

class SimpleMissClickConsumer(missClickDao: MissClickDao,
                              backgroundScheduler: Scheduler) : MissClickEventsConsumer,
        BaseConsumer<MissClickEntity>(missClickDao, backgroundScheduler) {


    override fun onConsume(timestamp: Long, distance: Double, missClickCount: Int) {
        this.onNewItem(MissClickEntity(null, timestamp, distance))
    }

}