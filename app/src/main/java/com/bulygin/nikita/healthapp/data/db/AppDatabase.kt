package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [TypingErrorsEntity::class,
    TremorEventEntity::class,
    MissClickEntity::class,
    UserActivityEntity::class,
    OrientationEntity::class], version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun typingErrorDao(): TypingErrorsDao
    abstract fun missClickDao(): MissClickDao
    abstract fun userActivityDao(): UserActivityDao
    abstract fun orientationDao(): OrientationDao
    abstract fun getTremorDao():TremorDao
}