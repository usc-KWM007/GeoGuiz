package com.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

const val IS_CHEATER_KEY = "IS_CHEATER_KEY"


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, answer = true, answered = false, cheated = false),
        Question(R.string.question_oceans, answer = true, answered = false, cheated = false),
        Question(R.string.question_mideast, answer = false, answered = false, cheated = false),
        Question(R.string.question_africa, answer = false, answered = false, cheated = false),
        Question(R.string.question_americas, answer = true, answered = false, cheated = false),
        Question(R.string.question_asia, answer = true, answered = false, cheated = false)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    fun cheated_Question(){
        questionBank[currentIndex].cheated = true
    }

    fun checkCheated_Question(): Boolean{
        return questionBank[currentIndex].cheated
    }

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var correctlyAnswered = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        } else {
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }

    fun correctlyAnswered() {
        correctlyAnswered += 1
    }

    fun answered() {
        questionBank[currentIndex].answered = true
    }

    fun checkIfAnswered(): Boolean {
        return questionBank[currentIndex].answered
    }

    fun checkAllAnswered(): Boolean {
        var count = 0
        for (question in questionBank) {
            count += 1
            if (!question.answered) {
                return false
            }
        }
        return true
    }

    fun calculateResult(): Float {
        return (correctlyAnswered.toFloat() / questionBank.size) * 100
    }
}
