package ru.etu.parkinsonlibrary.rotation

import android.R
import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import io.reactivex.disposables.Disposable
import ru.etu.parkinsonlibrary.database.OrientationEntity
import ru.etu.parkinsonlibrary.di.DependencyProducer


/**
 * Сервис, который постоянно мониторинг углы наклона устройств
 * и записывает их в базу данных.
 * Данные записываются в базу данных каждую секунду,
 * при изменении значений. Если телефон находится в одинаковом положении,
 * т.е его углы наклона не меняются, то данные в базу не сохраняются.
 * то данные не записываются в базу
 */
class RotationDetectorService : Service() {

    private var rotationSubscription: Disposable? = null

    private var currentOrientation: List<Int>? = null
    private var lastOrientation: List<Int>? = null

    override fun onCreate() {
        super.onCreate()
        val module = DependencyProducer(this.application)
        val uiScheduler = module.getUIScheduler()
        val rotationDetector = module.getRotationDetector(this)
        rotationSubscription = rotationDetector.getOrientation().observeOn(uiScheduler).subscribe({ result ->
            currentOrientation = result.map { it.toInt() }
            if (currentOrientation != lastOrientation) {
                rotationDetector.onNewItem(OrientationEntity(null, System.currentTimeMillis(),
                        currentOrientation!![0], currentOrientation!![1], currentOrientation!![2]))
            }
            lastOrientation = currentOrientation
        }, {
            it.printStackTrace()
            stopSelf()
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            moveToForeground()
        }
    }

    private fun moveToForeground() {
        val notificationManager = NotificationManager(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createMainNotificationChannel()
            val notification = NotificationCompat.Builder(this, notificationManager.getMainNotificationId())
                    .setSmallIcon(R.color.white)
                    .setContentTitle("Rotation ")
                    .setContentText("Monitoring rotation is working")
                    .build()
            startForeground(1224, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        rotationSubscription?.dispose()
        val broadcastIntent = Intent("ru.etu.parkinsonlibrary.rotation.RestartRotationServiceReceiver")
        sendBroadcast(broadcastIntent)
    }
}


class NotificationManager(private val context: Context) {

    companion object {
        private val CHANNEL_ID = "Rotation"
        private val CHANNEL_NAME = "Rotation service chanel"
        private val CHANNEL_DESCRIPTION = "Chanel for rotation service, if it's need to be configure like a foreground"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMainNotificationId(): String {
        return CHANNEL_ID
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMainNotificationChannel() {
        val id = CHANNEL_ID
        val name = CHANNEL_NAME
        val description = CHANNEL_DESCRIPTION
        val importance = android.app.NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val existingChanel = mNotificationManager.getNotificationChannel(CHANNEL_ID)
        if (existingChanel == null) {
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }
}