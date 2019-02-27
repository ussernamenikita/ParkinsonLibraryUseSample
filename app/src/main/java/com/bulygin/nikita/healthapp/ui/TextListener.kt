package com.bulygin.nikita.healthapp.ui

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

private const val EMPTY = -1


class TextListener(var consumer: TypingErrorConsumer? = null) : TextWatcher {

    private var eraseCount = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        println("string = $s,start = $start,count = $count, after = $after")
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    private var previousLastWord: CharSequence? = null

    private var lastActionIsErase: Boolean = false

    override fun afterTextChanged(s: Editable) {
        if(s.isEmpty() || s[s.length-1] == ' '){
            if(eraseCount>0 && !lastActionIsErase){
                this.consumer?.onWordTypingEnd(s.toString(),eraseCount)
            }
            eraseCount = 0
            previousLastWord = null
            return
        }
        val currentWord = getLastWord(s)
        if(currentWord != null && previousLastWord != null){
            lastActionIsErase = if(previousLastWord!!.length>currentWord.length){
                eraseCount++
                true
            }else{
                false
            }
        }
        this.previousLastWord = currentWord
    }

    private fun getLastWord(s: Editable): CharSequence? {
        val lastIndex = s.lastIndexOf(" ")
        if(lastIndex == -1){
            return if( s.isNotEmpty()) {
                s
            }else{
                null
            }
        }
        return s.subSequence(lastIndex,s.length)
    }

    interface TypingErrorConsumer {
        fun onWordTypingEnd(s: String, eraseCount: Int)
    }
}
