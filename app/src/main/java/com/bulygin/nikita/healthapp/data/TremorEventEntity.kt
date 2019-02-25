package com.bulygin.nikita.healthapp.data

import android.arch.persistence.room.*

@Entity
data class TremorEventEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                             @ColumnInfo(name = "timeStamp") var timeStamp: Long,
                             @ColumnInfo(name = "force") var force: Float,
                             @ColumnInfo(name = "count") var shakeCount: Int)

@Dao
interface TremorDao : BaseDao<TremorEventEntity> {

    @Query("SELECT * FROM TremorEventEntity")
    fun getAll(): List<TremorEventEntity>

    @Query("DELETE FROM TremorEventEntity")
    fun clear()
}

@Database(entities = arrayOf(TremorEventEntity::class, TypingErrorsEntity::class,MissClickEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tremorDao(): TremorDao
    abstract fun typingErrorDao(): TypingErrorsDao
    abstract fun missClickDao():MissClickDao
}

