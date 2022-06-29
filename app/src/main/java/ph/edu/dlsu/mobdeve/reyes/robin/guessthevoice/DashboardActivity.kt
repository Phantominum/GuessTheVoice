package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
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
    private var email: String? = null
    private var userID : String? = null
    private var topGenres = ArrayList<Genre>()
    private lateinit var user: User
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
            email = bundle.getString("email").toString()
            println("LOG: Dashboard bundle got $email")
            setUser(email)
        } else {
            // TODO: Remove this when done testing
            email="gimmba@gim.com"
            setUser("gimmba@gim.com")
        }
        // Populate genres
        populateGenres()
        // Top Genre 1
        binding.genre1.setOnClickListener{
            val topBundle1 = Bundle()
            topBundle1.putString("email", email)
            topBundle1.putString("genre_name", topGenres.get(0).genre_name)
            topBundle1.putString("genre_color", topGenres.get(0).genre_color)
            var goToQuizzes = Intent(this, Quizzes::class.java)
            goToQuizzes.putExtras(topBundle1)
            startActivity(goToQuizzes)
//            finish()
        }

        binding.genre2.setOnClickListener{
            val topBundle2 = Bundle()
            topBundle2.putString("email", email)
            topBundle2.putString("genre_name", topGenres.get(1).genre_name)
            topBundle2.putString("genre_color", topGenres.get(1).genre_color)
            var goToQuizzes = Intent(this, Quizzes::class.java)
            goToQuizzes.putExtras(topBundle2)
            startActivity(goToQuizzes)
//            finish()
        }

        binding.buttonSettings.setOnClickListener{
            var goToSettings = Intent(this, Settings::class.java)
            startActivity(goToSettings)
        }

        binding.buttonQuizMaker.setOnClickListener{
            if (userID != null) {
                val quizBundle = Bundle()
                quizBundle.putString("userID",userID)
                quizBundle.putString("email", email)
                val goToQuizMaker = Intent(this, QuizMaker::class.java)
                goToQuizMaker.putExtras(quizBundle)
                startActivity(goToQuizMaker)
//                finish()
            }

        }

        binding.buttonLikedQuizzes.setOnClickListener{
            var goToLikedQuizzes = Intent(this, LikedQuizzesActivity::class.java)
            startActivity(goToLikedQuizzes)
        }
    }

    fun setUser(email: String?) {
        if (email != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                val docID = async { dao.getAccountDocID(email) }
                // Assign document id to userID
                userID = docID.await()
                // Get user metadata
                val userJob = async { dao.getAccount(email) }
                user = userJob.await()!!
                println("LOG: User data is found ${user.username}")
                for (genreName in user.top_genres) {
                    // Retrieve genre metadata
                    val genreJob = async { genredao.getGenre(genreName) }
                    if (genreJob.await() != null) {
                        println("LOG: Top genre found ${genreJob.await()!!.genre_name}")
                        topGenres.add(genreJob.await()!!)
                    }
                    else
                       println("LOG: No top genres found")
                }

                withContext(Dispatchers.Main) {
                    // Update UI of top genres
                    binding.genre1Name.text = topGenres.get(0).genre_name
                    binding.genre2Name.text = topGenres.get(1).genre_name
                    // TODO: Update color according to the genre_color attrib
                    binding.genre1.setBackgroundColor(Color.parseColor("#808080"))
                }
            }
        }
    }
//
//    fun populateTopGenres() {
//        lifecycleScope.async(Dispatchers.IO) {
//            // Iterate each genre in user's top genres
//            for (genreName in user.topGenres) {
//                // Retrieve genre metadata
//                val genreJob = async { genredao.getGenre(genreName) }
//                if (genreJob.await() != null)
//                    topGenres.add(genreJob.await()!!)
//            }
//
//            withContext(Dispatchers.Main) {
//                // Update UI of top genres
//                binding.genre1Name.text = topGenres.get(0).genre_name
//                binding.genre2Name.text = topGenres.get(1).genre_name
//                // TODO: Update color according to the genre_color attrib
//            }
//        }
//    }
 
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