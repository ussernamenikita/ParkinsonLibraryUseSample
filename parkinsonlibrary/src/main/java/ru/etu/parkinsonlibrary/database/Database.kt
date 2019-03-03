package ru.etu.parkinsonlibrary.database

import android.arch.persistence.room.*
import io.reactivex.Single

@Database(entities = [MissClickEntity::class, TypingErrorEntity::class], version = 2, exportSchema = false)
abstract class ParkinsonLibraryDatabase : RoomDatabase() {
    abstract fun missClickDao(): MissClickDao
    abstract fun typingErrorDao(): TypingErrorsDao
    abstract fun getOrientatoinDao():OrientationDao
}

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: T)
}

@Entity
data class MissClickEntity(@PrimaryKey(autoGenerate = true) val id: Long? = 0L,
                           @ColumnInfo(name = "timestamp") var timestamp: Long,
                           @ColumnInfo(name = "distance") var distance: Double)

@Dao
interface MissClickDao : BaseDao<MissClickEntity> {

    @Query("DELETE FROM MissClickEntity")
    fun clear()

    @Query("SELECT * FROM MissClickEntity")
    fun getAll(): List<MissClickEntity>


    @Query("SELECT * FROM MissClickEntity")
    fun getAllAsSingle(): Single<List<MissClickEntity>>


}

@Entity
data class TypingErrorEntity(@PrimaryKey(autoGenerate = true) val id: Long? = 0L,
                             @ColumnInfo(name = "timeStamp") val timestamp: Long,
                             @ColumnInfo(name = "symbol") val charSequence: String,
                             @ColumnInfo(name = "timeChanges") val timeChanges: Long)

@Dao
interface TypingErrorsDao : BaseDao<TypingErrorEntity> {

    @Query("DELETE FROM TypingErrorEntity")
    fun clear()

    @Query("SELECT * FROM TypingErrorEntity")
    fun getAll(): List<TypingErrorEntity>

    @Query("SELECT * FROM TypingErrorEntity")
    fun getAllAsSingle(): Single<List<TypingErrorEntity>>
}

@Entity
data class OrientationEntity(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                             @ColumnInfo(name = "timestamp") val timestamp: Long,
                             @ColumnInfo(name = "x") val x: Float,
                             @ColumnInfo(name = "y") val y: Float,
                             @ColumnInfo(name = "z") val z: Float)

@Dao
interface OrientationDao : BaseDao<OrientationEntity> {

    @Query("DELETE FROM OrientationEntity")
    fun clear()

    @Query("SELECT * FROM OrientationEntity")
    fun getAll(): List<OrientationEntity>
}

