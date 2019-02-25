package com.bulygin.nikita.healthapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.data.SimpleTypingErrorsConsumer
import com.bulygin.nikita.healthapp.di.AppModule

class TypingErrorsFragment : Fragment(), TextListener.TypingErrorConsumer {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.inject()
    }

    private lateinit var module: AppModule

    private fun inject() {
        if (activity == null) {
            return
        }
        if (activity is MainActivity) {
            this.module = (activity as MainActivity).module
        }
        textListener = TextListener(this)
        this.typingErrorsConsumer = module.createTypingErrorConsumer()
    }

    lateinit var textListener: TextListener
    lateinit var typingErrorsConsumer: SimpleTypingErrorsConsumer

    private lateinit var typingErrorsCount: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.typing_errors_fragment, container, false)
        val editText = rootView.findViewById<EditText>(R.id.typing_errors_edit_text)
        this.typingErrorsCount = rootView.findViewById(R.id.typing_erorrs_count_tv)
        editText.addTextChangedListener(textListener)
        textListener.consumer = this
        updateCountText(0)
        return rootView
    }


    override fun onWordTypingEnd(s: String, eraseCount: Int) {
        if (activity != null) {
            updateCountText(eraseCount)
        }
        typingErrorsConsumer.onWordTypingEnd(s, eraseCount)
    }

    private fun updateCountText(eraseCount: Int) {
        typingErrorsCount.text = activity!!.getString(R.string.typing_errors_count_text, eraseCount)
    }


}