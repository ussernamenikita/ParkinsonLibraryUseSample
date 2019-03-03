package ru.etu.parkinsonlibrary

import io.reactivex.Single

interface EntityToCsv<T> {
    fun toCsv(items:List<T>):String
    fun getAsCsv(): Single<String>
}
