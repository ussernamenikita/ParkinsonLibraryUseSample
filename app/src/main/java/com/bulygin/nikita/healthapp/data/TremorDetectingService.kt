package com.bulygin.nikita.healthapp.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.data.db.AppDatabase


class TremorDetectingService : Service() {

    private var tremorListener: TremorListener? = null

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mDatabase: AppDatabase? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initDb()
        initListener()
        buildNotifcationForAPiAfterO()
        return Service.START_STICKY
    }

    private fun initListener() {
        if (tremorListener == null) {
            return
        }
        tremorListener = TremorListener(this, mDatabase!!.getTremorDao())
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager!!.registerListener(tremorListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI, Handler())

    }

    private fun buildNotifcationForAPiAfterO() {
        initChannels(this)
        if (Build.VERSION.SDK_INT >= 26) {
            val mBuilder = NotificationCompat.Builder(this, "default")
            mBuilder.setSmallIcon(R.drawable.ic_launcher_background)
            mBuilder.setContentTitle("Notification Alert, Click Me!")
            mBuilder.setContentText("Hi, This is Android Notification Detail!")
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(100, mBuilder.build())
            startForeground(100, mBuilder.build())
        }
    }

    private fun initDb() {
        if (mDatabase == null) {
            mDatabase = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java, "database-name"
            ).build()
        }
    }

    fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("default", "TremorService",
                NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Chanel for tremor service"
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager?.unregisterListener(tremorListener)
    }
}