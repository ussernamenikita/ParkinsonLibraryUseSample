package ru.etu.parkinsonlibrary.database.consumer

import android.location.Location
import io.reactivex.Scheduler
import ru.etu.parkinsonlibrary.coordinate.LocationConsumer
import ru.etu.parkinsonlibrary.database.OrientationDao
import ru.etu.parkinsonlibrary.database.OrientationEntity
import java.util.concurrent.atomic.AtomicReference

/**
 * Consumer сохраняет данные о наклоне устройсва в базу данных, если они изменились
 */
class DatabaseRotationConsumer(dao: OrientationDao,
                               netScheduler: Scheduler) :
        BaseConsumer<OrientationEntity>(dao, netScheduler), LocationConsumer {

    private val currentLocation = AtomicReference<Location>()

    override fun onLocation(location: Location?) {
        location?.let { currentLocation.set(it) }
    }

    private var currentOrientation: List<Int>? = null

    private var lastOrientation: List<Int>? = null

    private var lastSavedLocation: Location? = null

    fun onNewAngels(data: Array<Float>) {
        this.currentOrientation = data.map { it.toInt() }
        val location = currentLocation.get()
        if (isEquals(currentOrientation, lastOrientation) || isEquals(lastSavedLocation, location)) {
            onNewItem(OrientationEntity(null, System.currentTimeMillis(),
                    currentOrientation!![0],
                    currentOrientation!![1],
                    currentOrientation!![2],
                    latitude = location?.latitude,
                    longitude = location?.longitude,
                    altitude = location?.altitude))
        }
        this.lastOrientation = currentOrientation
        this.lastSavedLocation = currentLocation.get()
    }

    private fun isEquals(o1: Location?, o2: Location?): Boolean {
        if (o1 == null && o2 == null) {
            return true
        }
        if (o1 != null) {
            if (o2 != null) {
                return o1.latitude == o1.latitude &&
                        o1.longitude == o1.longitude &&
                        o1.altitude == o1.altitude
            }
        }
        return false
    }

    private fun isEquals(currentOrientation: List<Int>?, lastOrientation: List<Int>?): Boolean {
        if (currentOrientation == null && lastOrientation == null) {
            return true
        }
        if (currentOrientation != null) {
            if (lastOrientation != null) {
                return lastOrientation.containsAll(currentOrientation) && currentOrientation.containsAll(lastOrientation)
            }
        }
        return false
    }
}