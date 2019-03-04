package com.bulygin.nikita.healthapp.di

import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.ui.*
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class AppModule(private val activity: MainActivity) {


    fun createHealthPagerAdapter(): HealthPagerAdapter {
        return HealthPagerAdapter(activity.supportFragmentManager,
                arrayOf(MissClickFragment(), TypingErrorsFragment(), RotationDetectingFragment()),
                arrayOf(activity.getString(R.string.miss_click_title),
                        activity.getString(R.string.typing_error_title),
                        activity.getString(R.string.rotation_detector_title)))
    }


    fun getUIScheduler(): Scheduler = AndroidSchedulers.mainThread()


}
