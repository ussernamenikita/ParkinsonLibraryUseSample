package com.bulygin.nikita.healthapp.data

import android.content.Context
import com.bulygin.nikita.healthapp.data.db.UserActivityDao
import com.bulygin.nikita.healthapp.data.db.UserActivityEntity
import com.bulygin.nikita.healthapp.domain.UserActivity
import com.bulygin.nikita.healthapp.domain.UserActivityRepository
import com.google.android.gms.location.DetectedActivity
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.rx.ObservableFactory
import io.reactivex.Observable
import io.reactivex.Scheduler

class UserActivityRepositoryImplementation(private val context: Context,
                                           userActivityDao: UserActivityDao,
                                           backgroundScheduler: Scheduler) : UserActivityRepository,
        BaseConsumer<UserActivityEntity>(userActivityDao, backgroundScheduler) {
    override fun getActionsObservable(): Observable<DetectedActivity> = ObservableFactory.from(SmartLocation.with(context).activity())

    override fun saveUserActivity(activity: UserActivity) {
        onNewItem(UserActivityEntity(null, activity.type, activity.timestamp))
    }
}