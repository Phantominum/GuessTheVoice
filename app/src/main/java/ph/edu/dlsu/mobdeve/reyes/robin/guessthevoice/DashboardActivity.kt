package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.GenreAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAOArrayImpl
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityDashboardBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var genreArrayList: ArrayList<Genre>
    private var user: User? = null
    private lateinit var dao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize dao
        dao = UserDAO(applicationContext)
        // Retrieve username from bundle
        var bundle = intent.extras
        if (bundle != null) {
            // Get the user metadata
            user = dao.getAccount(bundle.getString("username").toString())
            if (user != null)
                println("LOG: Dashboard retrieved user data")
        }
        //TODO: Remove init()
        init()
        binding.genreList.layoutManager = GridLayoutManager(applicationContext,2)
        genreAdapter = GenreAdapter(applicationContext, genreArrayList)
        binding.genreList.adapter = genreAdapter

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

    private fun init(){
        val dao : GenreDAO = GenreDAOArrayImpl()
        var genre =  Genre()

        genre.genre_name = "Rock"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "Blues"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "Rap"
        genre.genre_color = "green"
        dao.addGenre(genre)


        genre =  Genre()
        genre.genre_name = "K-Pop"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "J-Pop"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "Classical"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "Video Games"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genre =  Genre()
        genre.genre_name = "Musical"
        genre.genre_color = "green"
        dao.addGenre(genre)

        genreArrayList = dao.getGenres()
    }
}