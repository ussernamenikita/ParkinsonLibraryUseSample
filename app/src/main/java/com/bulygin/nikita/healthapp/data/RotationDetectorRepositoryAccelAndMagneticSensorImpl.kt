package com.bulygin.nikita.healthapp.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import com.bulygin.nikita.healthapp.data.db.BaseDao
import com.bulygin.nikita.healthapp.data.db.OrientationEntity
import com.bulygin.nikita.healthapp.domain.IRotationDetectorRepository
import com.bulygin.nikita.healthapp.domain.Orientation
import io.reactivex.Observable
import io.reactivex.Scheduler


class RotationDetectorRepositoryAccelAndMagneticSensorImpl(activity: AppCompatActivity, dao: BaseDao<OrientationEntity>, scheduler: Scheduler) : IRotationDetectorRepository, BaseConsumer<OrientationEntity>(dao, scheduler) {


    private val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val accelerationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val windowManager = activity.windowManager

    override fun getOrientation(): Observable<Array<Float>> {
        getSensorList()
        var sensorEventListener: SensorEventListener? = null
        val sensorObs = Observable.create<Array<Float>> { emitter ->
            sensorEventListener = object : SensorEventListener {

                private var magneticValue: FloatArray? = null
                private var accelerationValue: FloatArray? = null

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null && !emitter.isDisposed) {
                        when (event.sensor.type) {
                            Sensor.TYPE_ACCELEROMETER -> accelerationValue = event.values
                            Sensor.TYPE_MAGNETIC_FIELD -> magneticValue = event.values
                        }
                        val d = getDataFromSensors(magneticValue, accelerationValue)
                        d?.let { emitter.onNext(d) }
                    }
                }

            }
            sensorManager.registerListener(sensorEventListener, magneticSensor, 320000)
            sensorManager.registerListener(sensorEventListener, accelerationSensor, 320000)
        }

        return sensorObs.doOnDispose {
            sensorEventListener?.let {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }
    }

    private fun getDataFromSensors(magneticValue: FloatArray?, accelerationValue: FloatArray?): Array<Float>? {
        if (magneticValue == null || accelerationValue == null) {
            return null
        }
        val rotationMatrix = FloatArray(9)
        val rotationOK = SensorManager.getRotationMatrix(rotationMatrix, null, accelerationValue, magneticValue)
        val orientationValues = FloatArray(3)
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues)
            return arrayOf(orientationValues[0] * (180f / 3.14159265358979323846f), orientationValues[1] * (180f / 3.14159265358979323846f), orientationValues[2] * (180f / 3.14159265358979323846f))
        } else {
            return null
        }
    }


    private fun getSensorList() {

        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        val strLog = StringBuilder()
        var iIndex = 1
        for (item in sensors) {
            strLog.append(iIndex.toString() + ".")
            strLog.append(" Sensor Type - " + item.type + "\r\n")
            strLog.append(" Sensor Name - " + item.name + "\r\n")
            strLog.append(" Sensor Version - " + item.version + "\r\n")
            strLog.append(" Sensor Vendor - " + item.vendor + "\r\n")
            strLog.append(" Maximum Range - " + item.maximumRange + "\r\n")
            strLog.append(" Minimum Delay - " + item.minDelay + "\r\n")
            strLog.append(" Power - " + item.power + "\r\n")
            strLog.append(" Resolution - " + item.resolution + "\r\n")
            strLog.append("\r\n")
            iIndex++
        }
        println(strLog.toString())
    }

    override fun saveOrientationToDatabase(orientation: Orientation) {
        this.onNewItem(OrientationEntity(null, orientation.timestamp, arrayOf(orientation.x, orientation.y, orientation.z)))
    }

}

/*
return Observable.create(subscriber -> {

            val listener = SensorEventListener = {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }

                    subscriber.onNext(event);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // NO-OP
                }
            };

            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)

            // unregister listener in main thread when being unsubscribed
            subscriber.add(new MainThreadSubscription() {
                @Override
                protected void onUnsubscribe() {
                    sensorManager.unregisterListener(listener);
                }
            });
        });
 */