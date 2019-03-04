package ru.etu.parkinsonlibrary.database.consumer

import io.reactivex.Scheduler
import ru.etu.parkinsonlibrary.database.TypingErrorEntity
import ru.etu.parkinsonlibrary.database.TypingErrorsDao
import ru.etu.parkinsonlibrary.typingerror.TypingErrorTextListener

/**
 * Consumer для событий ошибок печати,
 * Записывает полученные события в базу данных.
 */
class DatabaseTypingErrorConsumer(typeErrorDao: TypingErrorsDao,
                                  backgroundScheduler: Scheduler) : TypingErrorTextListener.TypingErrorConsumer,
        BaseConsumer<TypingErrorEntity>(typeErrorDao, backgroundScheduler) {


    override fun onEvent(currentTimestamp: Long, changes: CharSequence, l: Long) {
        this.onNewItem(TypingErrorEntity(null, currentTimestamp, changes.toString(), l))
    }


}
