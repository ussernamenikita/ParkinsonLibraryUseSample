package com.bulygin.nikita.healthapp.domain

import io.reactivex.Observable

interface IRotationDetectorRepository {
    fun getOrientation():Observable<Array<Float>>
}
