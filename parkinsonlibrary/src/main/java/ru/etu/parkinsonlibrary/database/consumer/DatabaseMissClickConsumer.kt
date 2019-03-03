package ru.etu.parkinsonlibrary.database.consumer

import io.reactivex.Scheduler
import io.reactivex.Single
import ru.etu.parkinsonlibrary.EntityToCsv
import ru.etu.parkinsonlibrary.database.MissClickDao
import ru.etu.parkinsonlibrary.database.MissClickEntity
import ru.etu.parkinsonlibrary.missclick.MissClickEventsConsumer

class DatabaseMissClickConsumer(private val missClickDao: MissClickDao,
                                private val backgroundScheduler: Scheduler) : MissClickEventsConsumer,
        BaseConsumer<MissClickEntity>(missClickDao, backgroundScheduler), EntityToCsv<MissClickEntity> {

    override fun getAsCsv(): Single<String> {
        return missClickDao.getAllAsSingle().subscribeOn(backgroundScheduler).map { toCsv(it) }.onErrorResumeNext(Single.just(""))
    }


    override fun toCsv(items: List<MissClickEntity>): String {
        val builder = StringBuilder()
        for (item in items) {
            if (builder.isNotEmpty()) {
                builder.append("\n")
            }
            builder.append(toString(item))
        }
        return builder.toString()
    }

    private fun toString(entity: MissClickEntity): String = "${entity.id},${entity.timestamp},${entity.distance}"


    override fun onConsume(timestamp: Long, distance: Double, missClickCount: Int) {
        this.onNewItem(MissClickEntity(null, timestamp, distance))
    }

}