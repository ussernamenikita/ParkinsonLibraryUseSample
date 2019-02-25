package com.bulygin.nikita.healthapp.ui

import android.text.Editable
import android.text.TextWatcher

private const val EMPTY = -1


class TextListener(var consumer: TypingErrorConsumer? = null) : TextWatcher {

    private var eraseCount = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        println("string = $s,start = $start,count = $count, after = $after")
    }

    private var startOfWord: Int = EMPTY

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        println("string = $s,start = $start,count = $count, before = $before")
        if (count == 1 && s[start] == ' ') {
            if (startOfWord != EMPTY) {
                val word = s.subSequence(startOfWord, start)
                println("TEST End of word $word with eraseCount = $eraseCount")
                consumer?.onWordTypingEnd(word.toString(), eraseCount)
                dropStartOfWordAndEraseCount()
            }
        } else {
            if (startOfWord != EMPTY && isErase(before, count)) {
                if (start <= startOfWord) {
                    println("TEST erase all word")
                    dropStartOfWordAndEraseCount()
                } else {
                    println("TEST One erase detected")
                    eraseCount++
                }
            }
            if ((start == 0 && s.length == count) || (start - 1 > 0 && s[start - 1] == ' ')) {
                println("TEST Start of new word")
                startOfWord = start
                eraseCount = 0
            }
        }
    }

    private fun dropStartOfWordAndEraseCount() {
        startOfWord = EMPTY
        eraseCount = 0
    }

    private fun isErase(before: Int, count: Int): Boolean = before == 1 && count == 0

    override fun afterTextChanged(s: Editable) {

    }

    interface TypingErrorConsumer {
        fun onWordTypingEnd(s: String, eraseCount: Int)
    }
}
