package com.bulygin.nikita.healthapp.domain

import io.reactivex.Observable

interface IRotationDetectorRepository {
    fun getOrientation():Observable<Array<Float>>
    fun saveOrientationToDatabase(orientation:Orientation)
}

data class Orientation(val timestamp:Long,val x:Float,val y:Float,val z:Float)
