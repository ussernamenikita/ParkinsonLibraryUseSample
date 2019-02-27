package com.bulygin.nikita.healthapp.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.data.db.BaseDao
import com.bulygin.nikita.healthapp.data.db.OrientationEntity
import com.bulygin.nikita.healthapp.domain.IRotationDetectorRepository
import com.bulygin.nikita.healthapp.domain.Orientation
import io.reactivex.Observable
import io.reactivex.Scheduler


class RotationDetectorRepositoryAccelAndMagneticSensorImpl(private val activity: AppCompatActivity, dao: BaseDao<OrientationEntity>, scheduler: Scheduler) : IRotationDetectorRepository, BaseConsumer<OrientationEntity>(dao, scheduler) {


    private val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    override fun getOrientation(): Observable<Array<Float>> {
        checkRotationSensor()
        var sensorEventListener: SensorEventListener? = null
        val sensorObs = Observable.create<Array<Float>> { emitter ->
            sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null && !emitter.isDisposed) {
                        val d = getDataFromSensors(event.values)
                        d?.let { emitter.onNext(d) }
                    }
                }

            }
            sensorManager.registerListener(sensorEventListener, rotationSensor, 16000)
        }

        return sensorObs.doOnDispose {
            sensorEventListener?.let {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }
    }

    private fun getDataFromSensors(rotationVector: FloatArray?): Array<Float>? {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

        val worldAxisForDeviceAxisX: Int = SensorManager.AXIS_X
        val worldAxisForDeviceAxisY: Int = SensorManager.AXIS_Z

        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix)

        // Transform rotation matrix into azimuth/pitch/roll
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)

        // Convert radians to degrees
        val pitch = orientation[1] * 180.0 / Math.PI
        val roll = orientation[2] * 180.0 / Math.PI
        val azimut = orientation[0] * 180.0 / Math.PI
        return arrayOf(pitch.toFloat(), azimut.toFloat(), roll.toFloat())
    }


    private fun checkRotationSensor() {
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        if (sensors.find { it.type == Sensor.TYPE_ROTATION_VECTOR } == null) {
            Toast.makeText(activity, activity.getString(R.string.no_sensor_detected_for_rotation_detection), Toast.LENGTH_LONG).show()
        }
    }

    override fun saveOrientationToDatabase(orientation: Orientation) {
        this.onNewItem(OrientationEntity(null, orientation.timestamp, orientation.x, orientation.y, orientation.z))
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