package com.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()


    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result from Cheat Activity
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.cheated_Question()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener {view: View ->
            checkAnswer(true)
            }

        binding.falseButton.setOnClickListener {view: View ->
            checkAnswer(false)
        }


        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            //start cheat-activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            cheatLauncher.launch(intent)

        }

        updateQuestion()

        binding.previousButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        updateQuestion()


        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun calculateResult(){
        if (quizViewModel.checkAllAnswered()){
            val result = quizViewModel.calculateResult()
            Toast.makeText(this, "You got $result%", Toast.LENGTH_SHORT)
                .show()
        }
        return
    }

    private fun checkAnswer(userAnswer: Boolean) {
        //check and calculate results
        calculateResult()
        if (quizViewModel.checkIfAnswered()){
            return
        }
        //change question to be answered
        quizViewModel.answered()

        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.checkCheated_Question() -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer){
            quizViewModel.correctlyAnswered()
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
        calculateResult()
    }
}