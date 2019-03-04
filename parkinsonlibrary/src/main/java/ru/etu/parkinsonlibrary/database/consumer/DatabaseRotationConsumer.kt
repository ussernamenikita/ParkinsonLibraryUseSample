package ru.etu.parkinsonlibrary.database.consumer

import io.reactivex.Scheduler
import ru.etu.parkinsonlibrary.database.OrientationDao
import ru.etu.parkinsonlibrary.database.OrientationEntity

/**
 * Consumer сохраняет данные о наклоне устройсва в базу данных, если они изменились
 */
class DatabaseRotationConsumer(dao: OrientationDao,
                               netScheduler: Scheduler) :
        BaseConsumer<OrientationEntity>(dao, netScheduler) {


    private var currentOrientation: List<Int>? = null

    private var lastOrientation: List<Int>? = null

    fun onNewAngels(data: Array<Float>) {
        this.currentOrientation = data.map { it.toInt() }
        if (currentOrientation != lastOrientation) {
            onNewItem(OrientationEntity(null, System.currentTimeMillis(),
                    currentOrientation!![0], currentOrientation!![1], currentOrientation!![2]))
        }
        this.lastOrientation = currentOrientation
    }
}