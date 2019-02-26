package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.*

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
}