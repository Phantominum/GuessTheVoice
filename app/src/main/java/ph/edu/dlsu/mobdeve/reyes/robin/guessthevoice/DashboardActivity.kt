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

        //populate genres
        populateGenres()

//        binding.genreList.layoutManager = GridLayoutManager(applicationContext,2)
//        genreAdapter = GenreAdapter(applicationContext, genreArrayList)
//        binding.genreList.adapter = genreAdapter

        binding.genre1.setOnClickListener{
            // TODO: Retrieve top genres from users then pass the bundle
            var goToQuizzes = Intent(this, Quizzes::class.java)
            startActivity(goToQuizzes)
            finish()
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
                finish()
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
            }
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