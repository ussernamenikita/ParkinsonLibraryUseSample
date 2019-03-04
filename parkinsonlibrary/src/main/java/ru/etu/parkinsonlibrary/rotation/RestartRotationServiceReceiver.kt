package ru.etu.parkinsonlibrary.rotation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RestartRotationServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context, RotationDetectorService::class.java))
    }

}