package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


data class QuestionAnswer(val question: String, val answer: String)

class MainActivity : AppCompatActivity() {
    val questionsAndAnswers = listOf(
        QuestionAnswer("Is Brasília the capital city of Brazil?", "YES"),
        QuestionAnswer("Is Tokyo the capital city of Japan?", "YES"),
        QuestionAnswer("Is Paris the capital city of Italy?", "NO"),
        QuestionAnswer("Is Cairo the capital city of Egypt?", "YES"),
        QuestionAnswer("Is New York the capital city of the United States?", "NO")
    )

    var numOfQuestions = questionsAndAnswers.size
    var currQuestionIndex = 0
    var userAnswers = Array<String>(numOfQuestions) { "" }
    var score = 0

    lateinit var questionTextView: TextView
    lateinit var yesButton: Button
    lateinit var noButton: Button

    fun calculateScore(): Int {
        for (i in userAnswers.indices) {
            if (userAnswers[i] == questionsAndAnswers[i].answer)
                score++
        }
        return score
    }

    fun loadQuestionAndUserAnswer() {
        questionTextView.text = questionsAndAnswers[currQuestionIndex].question
        if(userAnswers[currQuestionIndex] == "YES"){
            yesButton.setBackgroundColor(Color.YELLOW)
            noButton.setBackgroundColor(Color.GRAY)
        } else if(userAnswers[currQuestionIndex] == "NO"){
            noButton.setBackgroundColor(Color.YELLOW)
            yesButton.setBackgroundColor(Color.GRAY)
        } else {
            yesButton.setBackgroundColor(Color.GRAY)
            noButton.setBackgroundColor(Color.GRAY)
        }
    }

    fun buttonClicked(answer: String) {
        userAnswers[currQuestionIndex] = answer
        currQuestionIndex++
        if(currQuestionIndex < numOfQuestions)
            loadQuestionAndUserAnswer()
        else {
            score = calculateScore()
            val intent = Intent(applicationContext, SecondActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("question_num", numOfQuestions);
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        questionTextView = findViewById<TextView>(R.id.text_id)
        yesButton = findViewById<Button>(R.id.yes_button)
        noButton = findViewById<Button>(R.id.no_button)

        loadQuestionAndUserAnswer()

        yesButton.setOnClickListener{ buttonClicked("YES") }
        noButton.setOnClickListener{ buttonClicked("NO") }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currQuestionIndex--;
                loadQuestionAndUserAnswer()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_question_index", currQuestionIndex)
        outState.putStringArray("user_answers", userAnswers)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currQuestionIndex = savedInstanceState.getInt("current_question_index", 0)
        userAnswers = savedInstanceState.getStringArray("user_answers") ?: Array(numOfQuestions) { "" }
        loadQuestionAndUserAnswer()
    }
}