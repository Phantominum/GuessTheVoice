package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.QuizAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAOArrayImpl


import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizzesBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score

class Quizzes : AppCompatActivity() {
    private lateinit var binding: ActivityQuizzesBinding
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizArrayList: ArrayList<Quiz>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizzesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        val curr_user = "tabbeee"


        binding.quizList.layoutManager = LinearLayoutManager(applicationContext)
        quizAdapter = QuizAdapter(applicationContext, quizArrayList, curr_user)
        binding.quizList.adapter = quizAdapter

//        print("Size is ${quizArrayList.size}")
//        print(quizArrayList)
    }

    private fun init() {
        val dao: QuizDAO = QuizDAOArrayImpl()
        var quiz = Quiz()
        quiz.likes = 1
        quiz.quiz_creator = "vissariown"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quiz = Quiz()
        quiz.likes = 15
        quiz.quiz_creator = "tabbeee"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quiz = Quiz()
        quiz.likes = 10
        quiz.quiz_creator = "djase"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quiz = Quiz()
        quiz.likes = 35
        quiz.quiz_creator = "Naes"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quiz = Quiz()
        quiz.likes = 69
        quiz.quiz_creator = "WalkingIcedCoffee"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quiz = Quiz()
        quiz.likes = 15
        quiz.quiz_creator = "sushixuni"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quizArrayList = dao.getQuizzes()
    }
}