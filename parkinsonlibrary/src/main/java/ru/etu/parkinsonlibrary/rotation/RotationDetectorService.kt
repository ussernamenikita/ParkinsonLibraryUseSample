package ru.etu.parkinsonlibrary.rotation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.disposables.Disposable
import ru.etu.parkinsonlibrary.database.OrientationEntity
import ru.etu.parkinsonlibrary.di.DependencyProducer

class RotationDetectorService : Service() {

    private val rotationSubscription : Disposable? = null

    override fun onCreate() {
        super.onCreate()
        val module = DependencyProducer(this.application)
        val rotaionDetecotor =  module.getRotationDetecotr(this)
        rotaionDetecotor.getOrientation().subscribe({
            rotaionDetecotor.onNewItem(OrientationEntity(null,System.currentTimeMillis(),it[0],it[1],it[2]))
        },{

        })

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
}