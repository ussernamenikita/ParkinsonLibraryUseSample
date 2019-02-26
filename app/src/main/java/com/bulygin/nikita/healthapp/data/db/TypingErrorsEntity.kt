package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.*

@Entity
data class TypingErrorsEntity(@PrimaryKey(autoGenerate = true) var id: Long? = 0L,
                              @ColumnInfo(name = "timestamp") var timestamp: Long,
                              @ColumnInfo(name = "eraseCounts") var errorsCounts: Int)

@Dao
interface TypingErrorsDao : BaseDao<TypingErrorsEntity> {

    @Query("DELETE FROM TypingErrorsEntity")
    fun clear()

    @Query("SELECT * FROM TypingErrorsEntity")
    fun getAll(): List<TypingErrorsEntity>
}