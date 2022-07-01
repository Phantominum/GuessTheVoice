package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
    private lateinit var spinner: Spinner
    private var selectedSort = "Recency"
    private var userEmailG = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        var isDesc = 0
        var sorted = ArrayList<Quiz>()
        super.onCreate(savedInstanceState)
        binding = ActivityQuizzesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var userEmail : String
        lateinit var genre : String
        lateinit var genre_color : String

        val bundle = intent.extras
        spinner = binding.filter
        setupSpinner()
        if (bundle != null) {
            userEmail = bundle.getString("email").toString()
            genre = bundle.getString("genre_name").toString()
            genre_color = bundle.getString("genre_color").toString()
            println("LOG: Quizzes Bundle found $userEmail and $genre")
        } else {
            // TODO: Remove when done testing
            println("LOG: No bundle found in Quizzes")
            userEmail = "gimmba@gim.com"
            genre = "Pop"
            genre_color = "#808080"
        }

        userEmailG = userEmail

        // Setup dao
        genreDAO = GenreDAO(applicationContext)
        quizDAO = QuizDAO(applicationContext)
        // Update banner
        binding.bannerText.text = genre
        // TODO: Update banner color

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

    private fun setupSpinner() {
        val choices = arrayOf("Likes","Recency","Quiz Name")
        val ctx = applicationContext
        val arrayAdapter = ctx?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_dark_item,
                choices
            )
        }
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                selectedSort = choices[position]

                if(selectedSort=="Recency"){
                    quizArrayList.sortByDescending {
                        it.created_at
                    }
                    quizAdapter = QuizAdapter(applicationContext, quizArrayList, userEmailG)
                    binding.quizList.adapter = quizAdapter
                    binding.quizList.adapter!!.notifyDataSetChanged()
                }
                else if(selectedSort=="Likes"){
                    quizArrayList.sortByDescending {
                        it.likes
                    }
                    quizAdapter = QuizAdapter(applicationContext, quizArrayList, userEmailG)
                    binding.quizList.adapter = quizAdapter
                    binding.quizList.adapter!!.notifyDataSetChanged()
                }
                else{
                    quizArrayList.sortByDescending {
                        it.quiz_name
                    }
                    quizAdapter = QuizAdapter(applicationContext, quizArrayList, userEmailG)
                    binding.quizList.adapter = quizAdapter
                    binding.quizList.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }


}