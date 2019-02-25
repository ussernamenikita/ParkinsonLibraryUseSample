package com.bulygin.nikita.healthapp.di

import android.arch.persistence.room.Room
import com.bulygin.nikita.healthapp.ui.MainActivity
import com.bulygin.nikita.healthapp.ui.MissClickEventsConsumer
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.data.AppDatabase
import com.bulygin.nikita.healthapp.data.SimpleMissClickConsumer
import com.bulygin.nikita.healthapp.data.SimpleTypingErrorsConsumer
import com.bulygin.nikita.healthapp.ui.*
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
                arrayOf(MissClickFragment(), TypingErrorsFragment()),
                arrayOf(activity.getString(R.string.miss_click_title),activity.getString(R.string.typing_error_title)))
    }


}
