package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.QuizAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAOArrayImpl
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLikedQuizzesBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

class LikedQuizzesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikedQuizzesBinding
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizArrayList: ArrayList<Quiz>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLikedQuizzesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()


        binding.likedQuizList.layoutManager = LinearLayoutManager(applicationContext)
        quizAdapter = QuizAdapter(applicationContext, quizArrayList)
        binding.likedQuizList.adapter = quizAdapter
    }

    private fun init() {
        val dao: QuizDAO = QuizDAOArrayImpl()
        var quiz = Quiz()

        quiz = Quiz()
        quiz.likes = 15
        quiz.quiz_creator = "RandomUser"
        quiz.quiz_name = "Groovy Quiz"
        quiz.quiz_image = R.drawable.quizimage
        dao.addQuiz(quiz)

        quizArrayList = dao.getQuizzes()
    }

}