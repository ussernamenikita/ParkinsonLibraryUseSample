package ru.etu.parkinsonlibrary.coordinate

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment

class LocationPermissionRequer
private constructor(private val fragment: Fragment?, private val activity: Activity?, private val callback: Callback) {

    constructor(fragment: Fragment, callback: Callback) : this(fragment, null, callback)
    constructor(activity: Activity, callback: Callback) : this(null, activity, callback)


    private val locationPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> if (grantResults.isNotEmpty() && allGranted(grantResults)) {
                callback.onGranted()
            } else {
                callback.onDenied()
            }
        }
    }

    private fun allGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestPermissions() {
        if (itHaveAllPermissions()) {
            callback.onGranted()
        } else {
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, locationPermission, REQUEST_LOCATION_PERMISSION)
            } else
                fragment?.requestPermissions(locationPermission, REQUEST_LOCATION_PERMISSION)
        }
    }


    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 0x70CA
    }

    private fun itHaveAllPermissions(): Boolean {
        val localActivity = activity ?: fragment?.activity ?: return false
        for (perm in locationPermission) {
            if (ActivityCompat.checkSelfPermission(localActivity, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}

interface Callback {
    fun onGranted()
    fun onDenied()
}