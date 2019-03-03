package ru.etu.parkinsonlibrary.database.consumer

import io.reactivex.Scheduler
import io.reactivex.Single
import ru.etu.parkinsonlibrary.EntityToCsv
import ru.etu.parkinsonlibrary.database.TypingErrorEntity
import ru.etu.parkinsonlibrary.database.TypingErrorsDao
import ru.etu.parkinsonlibrary.typingerror.TypingErrorTextListeer

class DatabaseTypingErrorConsumer(private val typeErrorDao: TypingErrorsDao,
                                  private val backgroundScheduler: Scheduler) : TypingErrorTextListeer.TypingErrorConsumer,
        BaseConsumer<TypingErrorEntity>(typeErrorDao, backgroundScheduler), EntityToCsv<TypingErrorEntity> {

    override fun getAsCsv(): Single<String> {
        return typeErrorDao.getAllAsSingle().subscribeOn(backgroundScheduler).map { toCsv(it) }.onErrorResumeNext(Single.just(""))

    }

    override fun toCsv(items: List<TypingErrorEntity>): String {
        val stringBuilder = StringBuilder()
        for (item in items) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("\n")
            }
            stringBuilder.append(toString(item))
        }
        return stringBuilder.toString()
    }

    override fun onEvent(currentTimestamp: Long, changes: CharSequence, l: Long) {
        this.onNewItem(TypingErrorEntity(null, currentTimestamp, changes.toString(), l))
    }

    private fun toString(entity: TypingErrorEntity): String = "${entity.id},${entity.timestamp},${entity.charSequence},${entity.timeChanges}"


}
