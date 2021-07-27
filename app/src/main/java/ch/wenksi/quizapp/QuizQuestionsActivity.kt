package ch.wenksi.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var submitted: Boolean = false
    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Question>? = null
    private var selectedOptionPosition: Int = 0
    private var correctAnswers = 0
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        this.userName = intent.getStringExtra(Constants.USER_NAME)

        this.questionsList = Constants.getQuestions()
        setQuestion()
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> tv_option_one.background = ContextCompat.getDrawable(this, drawableView)
            2 -> tv_option_two.background = ContextCompat.getDrawable(this, drawableView)
            3 -> tv_option_three.background = ContextCompat.getDrawable(this, drawableView)
            4 -> tv_option_four.background = ContextCompat.getDrawable(this, drawableView)
        }
    }

    private fun setQuestion() {
        val question = this.questionsList!![this.currentPosition - 1]
        this.submitted = false
        defaultOptionsView()
        btn_submit.text = "SUBMIT"

        progressBar.progress = this.currentPosition
        tv_progress.text = "${this.currentPosition}/${progressBar.max}"
        iv_flag.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat
                .getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        if (this.submitted) return
        this.defaultOptionsView()
        this.selectedOptionPosition = selectedOptionNumber
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat
            .getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> this.selectedOptionView(tv_option_one, 1)
            R.id.tv_option_two -> this.selectedOptionView(tv_option_two, 2)
            R.id.tv_option_three -> this.selectedOptionView(tv_option_three, 3)
            R.id.tv_option_four -> this.selectedOptionView(tv_option_four, 4)
            R.id.btn_submit -> {
                if (this.submitted) { // set questions
                    this.currentPosition++
                    when {
                        this.currentPosition <= this.questionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, this.userName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, this.correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, this.questionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else if (this.selectedOptionPosition != 0) { // do nothing
                    val question = this.questionsList?.get(this.currentPosition - 1)
                    if (question!!.correctAnswer == this.selectedOptionPosition)
                        this.correctAnswers++
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    btn_submit.text = if (this.currentPosition == this.questionsList!!.size)
                        "QUIZ FINISHED" else "NEXT QUESTION"
                    this.selectedOptionPosition = 0
                    this.submitted = true
                }
            }
        }

    }
}
