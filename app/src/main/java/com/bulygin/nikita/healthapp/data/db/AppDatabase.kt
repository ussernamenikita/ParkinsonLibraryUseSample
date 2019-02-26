package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [TremorEventEntity::class, TypingErrorsEntity::class, MissClickEntity::class, UserActivityEntity::class, OrientationEntity::class], version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tremorDao(): TremorDao
    abstract fun typingErrorDao(): TypingErrorsDao
    abstract fun missClickDao(): MissClickDao
    abstract fun userActivityDao(): UserActivityDao
    abstract fun orientationDao(): OrientationDao
}