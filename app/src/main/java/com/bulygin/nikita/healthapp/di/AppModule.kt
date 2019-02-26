package com.bulygin.nikita.healthapp.di

import android.arch.persistence.room.Room
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.data.*
import com.bulygin.nikita.healthapp.domain.IRotationDetectorRepository
import com.bulygin.nikita.healthapp.domain.UserActivityRepository
import com.bulygin.nikita.healthapp.ui.*
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppModule(private val activity: MainActivity) {

    private var database: AppDatabase? = null


    fun createDatabase(): AppDatabase {
        if (this.database == null) {
            this.database = Room.databaseBuilder(
                    activity.application,
                    AppDatabase::class.java, "database-name"
            ).build()
        }
        return this.database!!
    }

    fun createTypingErrorConsumer(): SimpleTypingErrorsConsumer {
        return SimpleTypingErrorsConsumer(createDatabase().typingErrorDao(), Schedulers.computation())
    }

    fun createMissClickErrorConsumer(): MissClickEventsConsumer {
        return SimpleMissClickConsumer(createDatabase().missClickDao(), Schedulers.computation())
    }

    fun createHealthPagerAdapter(): HealthPagerAdapter {
        return HealthPagerAdapter(activity.supportFragmentManager,
                arrayOf(MissClickFragment(), TypingErrorsFragment(), UserActivityFragment(),RotationDetectingFragment()),
                arrayOf(activity.getString(R.string.miss_click_title),
                        activity.getString(R.string.typing_error_title),
                        activity.getString(R.string.user_activity_title),
                        activity.getString(R.string.rotation_detector_title)))
    }


    private var userActivityRepo: UserActivityRepositoryImplementation? = null

    fun createUserActivityRepo(): UserActivityRepository {
        if (userActivityRepo == null) {
            userActivityRepo = UserActivityRepositoryImplementation(activity, createDatabase().userActivityDao(), Schedulers.computation())
        }
        return userActivityRepo!!
    }

    fun getUIScheduler(): Scheduler = AndroidSchedulers.mainThread()

    fun getRotationDetectorRepo():IRotationDetectorRepository = RotationDetectorRepositoryAccelAndMagneticSensorImpl(activity)


}
