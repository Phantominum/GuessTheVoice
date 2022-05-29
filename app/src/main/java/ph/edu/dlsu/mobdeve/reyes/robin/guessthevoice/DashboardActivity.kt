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
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityDashboardBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var genreAdapter: GenreAdapter
    private lateinit var genreArrayList: ArrayList<Genre>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        binding.genreList.layoutManager = GridLayoutManager(applicationContext,2)
//        binding.genreList.layoutManager = LinearLayoutManager(applicationContext)
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


//        print("Size is ${genreArrayList.size}")
//        print(genreArrayList)

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