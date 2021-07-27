package ch.wenksi.quizapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Question>? = null
    private var selectedOptionPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        this.questionsList = Constants.getQuestions()
        setQuestion()
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
    }

    private fun setQuestion() {
        val question = this.questionsList!![this.currentPosition - 1]
        progressBar.progress = this.currentPosition
        tv_progress.text = "${this.currentPosition}/${progressBar.max}"
        tv_question.text = question.question
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
        this.defaultOptionsView()
        this.selectedOptionPosition = selectedOptionNumber
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat
            .getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_option_one -> this.selectedOptionView(tv_option_one, 1)
            R.id.tv_option_two -> this.selectedOptionView(tv_option_two, 2)
            R.id.tv_option_three -> this.selectedOptionView(tv_option_three, 3)
            R.id.tv_option_four -> this.selectedOptionView(tv_option_four, 4)
        }
    }

}
