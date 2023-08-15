package com.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val ANSWER_SHOWN_KEY = "ANSWER_SHOWN_KEY"

class CheatViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private var answerShown: Boolean
        get() = savedStateHandle.get(ANSWER_SHOWN_KEY) ?: false
        set(value) = savedStateHandle.set(ANSWER_SHOWN_KEY, value)


    fun setIfAnswerShown(){
        answerShown=true
    }
    fun getIfAnswerShown(): Boolean {
        return answerShown
    }
}