package com.bulygin.nikita.healthapp.data

import android.arch.persistence.room.*

@Entity
data class UserActivityEntity(@PrimaryKey(autoGenerate = true) val id: Long?,
                              @ColumnInfo(name = "type") val type: String,
                              @ColumnInfo(name = "timestamp") val timestamp: Long)

@Dao
interface UserActivityDao : BaseDao<UserActivityEntity> {
    @Query("SELECT * FROM UserActivityEntity")
    fun getAll(): List<UserActivityEntity>

    @Query("DELETE FROM UserActivityEntity")
    fun clear()
}