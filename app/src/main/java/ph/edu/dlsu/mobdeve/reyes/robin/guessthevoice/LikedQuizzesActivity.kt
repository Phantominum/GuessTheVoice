package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.QuizAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLikedQuizzesBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

class LikedQuizzesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikedQuizzesBinding
    private lateinit var quizAdapter: QuizAdapter
    private var quizArrayList: ArrayList<Quiz> = ArrayList()
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLikedQuizzesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize dao
        val userDAO = UserDAO(applicationContext)
        val quizDAO = QuizDAO(applicationContext)

        val bundle = intent.extras
        if (bundle != null) {
            email = bundle.getString("email").toString()
        } else {
            // TODO: Remove when done testing
            email = "gimmba@gim.com"
        }

        lifecycleScope.launch(Dispatchers.IO) {
            // Retrieve liked IDs
            val userJob = async{ userDAO.getLikedQuizzes(email)}
            val likedIDs = userJob.await()
            // Get the quiz data per id
            if (likedIDs != null) {
                for (id in likedIDs) {
                    // Retrieve quiz
                    val quizJob = async { quizDAO.getQuizById(id) }
                    val quiz = quizJob.await()
                    // Add to arraylist
                    if (quiz != null)
                        quizArrayList.add(quiz)
                }
            }
            // Add to adapter
            withContext(Dispatchers.Main) {
                binding.likedQuizList.layoutManager = LinearLayoutManager(applicationContext)
                quizAdapter = QuizAdapter(applicationContext, quizArrayList)
                binding.likedQuizList.adapter = quizAdapter
            }

        }


    }
}