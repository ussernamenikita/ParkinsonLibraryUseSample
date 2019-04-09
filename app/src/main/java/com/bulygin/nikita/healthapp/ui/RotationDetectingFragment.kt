package com.bulygin.nikita.healthapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.bulygin.nikita.healthapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.etu.parkinsonlibrary.coordinate.LocationPermissionRequer
import ru.etu.parkinsonlibrary.di.DependencyProducer
import ru.etu.parkinsonlibrary.coordinate.RotationCallback
import ru.etu.parkinsonlibrary.database.DatabaseHelper
import ru.etu.parkinsonlibrary.rotation.RotationDetectorService

class RotationDetectingFragment : Fragment(), RotationCallback {

    override fun onGranted() {
        val intent = Intent(activity, RotationDetectorService::class.java)
        intent.putExtra(RotationDetectorService.LOCATION_PERMISSIONS_KEY, true)
        activity?.startService(intent)
    }

    override fun onDenied() {
        Toast.makeText(activity, "Can't use gps without perrmissions", LENGTH_LONG).show()
    }

    lateinit var xValueTv: TextView
    lateinit var yValueTv: TextView
    lateinit var zValueTv: TextView
    lateinit var mContext: Context
    lateinit var uiScheduler: Scheduler

    lateinit var locationPermissionRequer: LocationPermissionRequer

    private lateinit var databaseHelper: DatabaseHelper

    fun inject() {
        mContext = context!!
        val activity = (activity as MainActivity)
        activity.startService(Intent(activity, RotationDetectorService::class.java))
        uiScheduler = AndroidSchedulers.mainThread()
        val producer = DependencyProducer(activity.application)
        this.databaseHelper = producer.getDatabaseHelper()
        locationPermissionRequer = producer.getLocationPermissionRequer(this, this)
        locationPermissionRequer.requestPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

    }

    private var subscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.rotation_detecting_fragment, container, false)
        xValueTv = rootView.findViewById(R.id.rotation_detecting_x_tv)
        yValueTv = rootView.findViewById(R.id.rotation_detecting_y_tv)
        zValueTv = rootView.findViewById(R.id.rotation_detecting_z)
        rootView.findViewById<Button>(R.id.button2).setOnClickListener { view ->
            if (subscription == null || subscription!!.isDisposed) {
                subscription = databaseHelper.getRotationAsCsv(false).subscribeOn(Schedulers.computation()).observeOn(uiScheduler).subscribe({
                    for (item in it) {
                        println(item)
                    }
                }, {
                    it.printStackTrace()
                })
            }
        }
        updateValues(0.0, 0.0, 0.0)
        return rootView
    }

    private fun updateValues(x: Double, y: Double, z: Double) {
        xValueTv.text = mContext.getString(R.string.rotation_x_value, toString(x))
        yValueTv.text = mContext.getString(R.string.rotation_y_value, toString(y))
        zValueTv.text = mContext.getString(R.string.rotation_z_value, toString(z))
    }

    private fun toString(value: Double): String = Math.abs(value).toInt().toString()


    override fun onDestroyView() {
        super.onDestroyView()
        subscription?.dispose()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.locationPermissionRequer.onRequestPermissionsResult(requestCode, arrayOf(*permissions), grantResults)
    }
}