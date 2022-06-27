package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.GenreAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAO

import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityDashboardBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var genreArrayList: ArrayList<Genre>
    val db = Firebase.firestore
    private var user: User? = null
    private lateinit var dao: UserDAO
    private lateinit var genredao : GenreDAO

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize dao
        dao = UserDAO(applicationContext)
        genredao = GenreDAO(applicationContext)
        // Retrieve username from bundle
        var bundle = intent.extras
        if (bundle != null) {
            // Get the user metadata
            user = dao.getAccount(bundle.getString("username").toString())
            if (user != null)
                println("LOG: Dashboard retrieved user data")
        }

        //populate genres
        populateGenres()

//        binding.genreList.layoutManager = GridLayoutManager(applicationContext,2)
//        genreAdapter = GenreAdapter(applicationContext, genreArrayList)
//        binding.genreList.adapter = genreAdapter

        binding.genre1.setOnClickListener{

            var goToQuizzes = Intent(this, Quizzes::class.java)
            startActivity(goToQuizzes)
        }

        binding.buttonSettings.setOnClickListener{
            var goToSettings = Intent(this, Settings::class.java)
            startActivity(goToSettings)
        }

        binding.buttonQuizMaker.setOnClickListener{
            var goToQuizMaker = Intent(this, QuizMaker::class.java)
            startActivity(goToQuizMaker)
        }

        binding.buttonLikedQuizzes.setOnClickListener{
            var goToLikedQuizzes = Intent(this, LikedQuizzesActivity::class.java)
            startActivity(goToLikedQuizzes)
        }
    }

    fun populateGenres(){
        lifecycleScope.launch(Dispatchers.IO){
            val tempList = async{genredao.getGenres()}
            if (tempList.await() != null) {
                genreArrayList = tempList.await()!!

                withContext(Dispatchers.Main){
                    binding.genreList.layoutManager = GridLayoutManager(applicationContext,2)
                    genreAdapter = GenreAdapter(applicationContext, genreArrayList)
                    binding.genreList.adapter = genreAdapter
                }
            }

        }
    }



}