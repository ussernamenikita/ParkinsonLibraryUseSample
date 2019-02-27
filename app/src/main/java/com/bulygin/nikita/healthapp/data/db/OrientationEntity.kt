package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.*

@Entity
data class OrientationEntity(@PrimaryKey(autoGenerate = true) val id:Long? = null,
                             @ColumnInfo(name = "timestamt") val timestamp:Long,
                             @ColumnInfo(name = "x") val x:Float,
                             @ColumnInfo(name = "y") val y:Float,
                             @ColumnInfo(name = "z") val z:Float)

@Dao
interface OrientationDao: BaseDao<OrientationEntity> {

    @Query("DELETE FROM OrientationEntity")
    fun clear()

    @Query("SELECT * FROM OrientationEntity")
    fun getAll(): List<OrientationEntity>
}