package ru.etu.parkinsonlibrary.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.etu.parkinsonlibrary.database.ParkinsonLibraryDatabase
import ru.etu.parkinsonlibrary.database.consumer.DatabaseMissClickConsumer
import ru.etu.parkinsonlibrary.database.consumer.DatabaseTypingErrorConsumer
import ru.etu.parkinsonlibrary.rotation.RotationDetector
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DependencyProducer(private val application: Application) {

    private var databaseInstance: ParkinsonLibraryDatabase? = null
    private var backGroundScheduler: Scheduler

    init {
        val service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), getThreadFactory())
        backGroundScheduler = Schedulers.from(service)
    }

    fun getDatabase(): ParkinsonLibraryDatabase {
        if (this.databaseInstance == null) {
            this.databaseInstance = Room.databaseBuilder(
                    application,
                    ParkinsonLibraryDatabase::class.java, "ParkinsonLibrary"
            ).fallbackToDestructiveMigration()
                    .build()
        }
        return this.databaseInstance!!
    }

    fun createDatabaseTypingErrorConsumer(): DatabaseTypingErrorConsumer {
        val database = getDatabase()
        return DatabaseTypingErrorConsumer(database.typingErrorDao(), backGroundScheduler)
    }

    fun createDatabaseMissclickConsumer(): DatabaseMissClickConsumer {
        val database = getDatabase()
        return DatabaseMissClickConsumer(database.missClickDao(), backGroundScheduler)
    }

    fun getThreadFactory(): ThreadFactory = NamedThreadFactory("ParkinsonLibrary")

    fun getRotationDetector(context: Context): RotationDetector {
        return RotationDetector(context, getDatabase().getOrientatoinDao(), backGroundScheduler, 1000)
    }

    fun getUIScheduler(): Scheduler = AndroidSchedulers.mainThread()
}

class NamedThreadFactory(private val mBaseName: String) : ThreadFactory {

    private val mDefaultThreadFactory: ThreadFactory = Executors.defaultThreadFactory()
    private val mCount = AtomicInteger(0)

    override fun newThread(runnable: Runnable): Thread {
        val thread = mDefaultThreadFactory.newThread(runnable)
        thread.name = mBaseName + "-" + mCount.getAndIncrement()
        return thread
    }
}

