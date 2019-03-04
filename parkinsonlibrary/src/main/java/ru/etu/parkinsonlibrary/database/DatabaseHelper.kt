package ru.etu.parkinsonlibrary.database

import io.reactivex.Scheduler
import io.reactivex.Single
import ru.etu.parkinsonlibrary.database.csv.EntityToCsv

/**
 * Класс-помощник для работы с базой данных,
 * позволяет получить сохраненные в базу данные в формате csv
 * и отчистить базу при необходимости
 */
class DatabaseHelper(private val missClickDao: MissClickDao,
                     private val rotationDao: OrientationDao,
                     private val typingErrorDao: TypingErrorsDao,
                     private val missClickToCsv: EntityToCsv<MissClickEntity>,
                     private val errorTypingToCsv: EntityToCsv<TypingErrorEntity>,
                     private val rotationToCsv: EntityToCsv<OrientationEntity>,
                     private val backgroundScheduler: Scheduler) {

    /**
     * Функция очищает все данные из базы данных,
     * возвращает true если очистка прошла успешно и false если нет
     */
    fun clearAllDataFromDatabase(): Single<Boolean> = Single.fromCallable {
        missClickDao.clear()
        rotationDao.clear()
        typingErrorDao.clear()
        true
    }.onErrorResumeNext(Single.just(false)).subscribeOn(backgroundScheduler)

    /**
     * Возвращает данные о непопадании на кнопки из базы дынных в формате csv
     * @param clearIfSuccess - если true, данные будут удалены поле получения,если false данные останятся в таблице
     */
    fun getMissClicksAsCsv(clearIfSuccess: Boolean): Single<String> =
            missClickToCsv.getAsCsv().doOnSuccess { if (clearIfSuccess) missClickDao.clear() }


    /**
     * Возвращает данные о ошибок печати из базы дынных в формате csv
     * @param clearIfSuccess - если true, данные будут удалены поле получения,если false данные останятся в таблице
     */
    fun getTypingErrorAsCsv(clearIfSuccess: Boolean): Single<String> =
            errorTypingToCsv.getAsCsv().doOnSuccess { if (clearIfSuccess) typingErrorDao.clear() }


    /**
     * Возвращает данные о поворатах устройства из базы дынных в формате csv
     * @param clearIfSuccess - если true, данные будут удалены поле получения,если false данные останятся в таблице
     */
    fun getRotationAsCsv(clearIfSuccess: Boolean): Single<String> =
            rotationToCsv.getAsCsv().doOnSuccess { if (clearIfSuccess) rotationDao.clear() }
}