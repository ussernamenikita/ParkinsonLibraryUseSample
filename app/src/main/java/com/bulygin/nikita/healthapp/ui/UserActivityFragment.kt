package com.bulygin.nikita.healthapp.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.domain.UserActivity
import com.bulygin.nikita.healthapp.domain.UserActivityRepository
import com.google.android.gms.location.DetectedActivity
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

class UserActivityFragment : Fragment() {

    private var subscription: Disposable? = null
    lateinit var uiScheduler: Scheduler
    private var lastActivityTv: TextView? = null
    private lateinit var myContext: Context
    private lateinit var simpleDateFormat: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        this.inject()
        super.onCreate(savedInstanceState)
        subscription = repo.getActionsObservable().observeOn(uiScheduler).subscribe({
            this.showDetectedActivity(it)
            repo.saveUserActivity(UserActivity(gteType(it.type), getCurrentTimestamp()))
        }, {
            it.printStackTrace()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.user_activity_fragment, container, false)
        this.lastActivityTv = rootView.findViewById(R.id.user_activity_info_tv)
        return rootView
    }

    private fun showDetectedActivity(it: DetectedActivity?) {
        if (it == null) {
            return
        }
        lastActivityTv?.text = myContext.getString(R.string.user_activity_last_activity_tv, gteType(it.type), getData(getCurrentTimestamp()))
    }

    private fun getData(currentTimestamp: Long): String = simpleDateFormat.format(Date(currentTimestamp))

    private fun gteType(type: Int): String {
        return when (type) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.UNKNOWN -> "UNKNOWN"
            DetectedActivity.TILTING -> "TILTING"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            else -> {
                "UNKNOWN"
            }
        }
    }

    private fun getCurrentTimestamp(): Long = System.currentTimeMillis()

    private lateinit var repo: UserActivityRepository

    private fun inject() {
        if (activity == null) {
            return
        }
        val module = (activity as MainActivity).module
        this.repo = module.createUserActivityRepo()
        this.uiScheduler = module.getUIScheduler()
        this.myContext = activity!!
        this.simpleDateFormat = SimpleDateFormat("HH:mm aaa, MMM dd,yyyy", Locale.ENGLISH)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.subscription?.dispose()
    }
}