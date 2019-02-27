package com.bulygin.nikita.healthapp.ui

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class TextListenerTest {

    private var listener = TextListener()
    private var eraseCounter = 0
    private var emptyConsumer = object : TextListener.TypingErrorConsumer {
        override fun onWordTypingEnd(s: String, eraseCount: Int) {
            eraseCounter = eraseCount
        }
    }

    @Before
    fun setUp() {
        listener.consumer = emptyConsumer
        this.eraseCounter = 0
    }

    @Test
    fun afterTextChanged_one_erase() {
        listener.afterTextChanged(StringEditable("error"))
        listener.afterTextChanged(StringEditable("erro"))
        listener.afterTextChanged(StringEditable("error"))
        listener.afterTextChanged(StringEditable("error "))
        assertEquals(1, eraseCounter)
    }

    @Test
    fun afterTextChanged_one_erase_two_words() {
        listener.afterTextChanged(StringEditable("error error"))
        listener.afterTextChanged(StringEditable("error erro"))
        listener.afterTextChanged(StringEditable("error error"))
        listener.afterTextChanged(StringEditable("error error "))
        assertEquals(1, eraseCounter)
    }

    @Test
    fun afterTextChanged_no_erase() {
        listener.afterTextChanged(StringEditable("error err"))
        listener.afterTextChanged(StringEditable("error erro"))
        listener.afterTextChanged(StringEditable("error error"))
        assertEquals(0, eraseCounter)
    }


    @Test
    fun afterTextChanged_two_erase() {
        listener.afterTextChanged(StringEditable("error"))
        listener.afterTextChanged(StringEditable("erro"))
        listener.afterTextChanged(StringEditable("err"))
        listener.afterTextChanged(StringEditable("erra"))
        listener.afterTextChanged(StringEditable("erral "))
        assertEquals(2, eraseCounter)
    }


    @Test
    fun afterTextChanged_two_erase_two_word() {
        listener.afterTextChanged(StringEditable("error error"))
        listener.afterTextChanged(StringEditable("error erro"))
        listener.afterTextChanged(StringEditable("error err"))
        listener.afterTextChanged(StringEditable("error erra"))
        listener.afterTextChanged(StringEditable("error erral "))
        assertEquals(2, eraseCounter)
    }

    @Test
    fun afterTextChanged_erase_all_words() {
        listener.afterTextChanged(StringEditable("or ror"))
        listener.afterTextChanged(StringEditable("or ro"))
        listener.afterTextChanged(StringEditable("or "))
        listener.afterTextChanged(StringEditable("or"))
        listener.afterTextChanged(StringEditable("o"))
        listener.afterTextChanged(StringEditable(""))
        assertEquals(0, eraseCounter)
    }
}