package com.bulygin.nikita.healthapp.data.db

import android.arch.persistence.room.*

@Entity
data class OrientationEntity(@PrimaryKey(autoGenerate = true) val id:Long? = null,
                             @ColumnInfo(name = "timestamt") val timestamp:Long,
                             @ColumnInfo(name = "angles") val angles:Array<Float>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrientationEntity

        if (id != other.id) return false
        if (timestamp != other.timestamp) return false
        if (!angles.contentEquals(other.angles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + angles.contentHashCode()
        return result
    }
}

@Dao
interface OrientationDao: BaseDao<OrientationEntity> {

    @Query("DELETE FROM OrientationEntity")
    fun clear()

    @Query("SELECT * FROM OrientationEntity")
    fun getAll(): List<OrientationEntity>
}