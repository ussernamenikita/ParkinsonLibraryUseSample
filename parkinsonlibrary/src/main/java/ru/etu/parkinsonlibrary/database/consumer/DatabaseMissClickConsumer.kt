package ru.etu.parkinsonlibrary.database.consumer

import io.reactivex.Scheduler
import io.reactivex.Single
import ru.etu.parkinsonlibrary.database.MissClickDao
import ru.etu.parkinsonlibrary.database.MissClickEntity
import ru.etu.parkinsonlibrary.missclick.MissClickEventsConsumer

class DatabaseMissClickConsumer(missClickDao: MissClickDao,
                                backgroundScheduler: Scheduler) : MissClickEventsConsumer,
        BaseConsumer<MissClickEntity>(missClickDao, backgroundScheduler) {


    override fun onConsume(timestamp: Long, distance: Double, missClickCount: Int) {
        this.onNewItem(MissClickEntity(null, timestamp, distance))
    }

}