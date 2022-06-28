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


import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizzesBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

class Quizzes : AppCompatActivity() {
    private lateinit var binding: ActivityQuizzesBinding
    private lateinit var quizAdapter: QuizAdapter
    private var quizArrayList: ArrayList<Quiz> = ArrayList()
    private lateinit var genreDAO: GenreDAO
    private lateinit var quizDAO: QuizDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizzesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO: Get user email and genre string from bundle
        lateinit var userEmail : String
        lateinit var genre : String
        val bundle = intent.extras
        if (bundle != null) {
            userEmail = bundle.getString("userEmail").toString()
            genre = bundle.getString("genre").toString()
        } else {
            userEmail = "gimmba@gim.com"
            genre = "Pop"
        }

        // Setup dao
        genreDAO = GenreDAO(applicationContext)
        quizDAO = QuizDAO(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            // Get all genre quizzes
            val genreJob = async{ genreDAO.getQuizzes(genre) }
            val quizIDs = genreJob.await()
            // If genre has quizzes, retrieve its metadata
            if (quizIDs != null) {
                // Get quiz object from db
                for (id in quizIDs){
                    val quizJob = async{ quizDAO.getQuizById(id)}
                    // Add quiz if it exists
                    if (quizJob.await() != null)
                        quizArrayList.add(quizJob.await()!!)
                    else
                        println("LOG: Quiz $id cannot be found in the database")
                }
                withContext(Dispatchers.Main) {
                    println("LOG: Genre $genre has ${quizArrayList.size} quizzes")
                    if(quizArrayList.size != 0) {
                        binding.quizList.layoutManager = LinearLayoutManager(applicationContext)
                        quizAdapter = QuizAdapter(applicationContext, quizArrayList, userEmail)
                        binding.quizList.adapter = quizAdapter
                    }
                }
            }
        }


    }
}