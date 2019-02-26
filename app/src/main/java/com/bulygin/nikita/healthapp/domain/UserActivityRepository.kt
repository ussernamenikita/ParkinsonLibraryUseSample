package com.bulygin.nikita.healthapp.domain

import com.google.android.gms.location.DetectedActivity
import io.reactivex.Observable

interface UserActivityRepository {
    fun getActionsObservable(): Observable<DetectedActivity>
    fun saveUserActivity(activity : UserActivity)

}
data class UserActivity(val type:String,val timestamp:Long)
