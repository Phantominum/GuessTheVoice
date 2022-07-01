package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.ScoreAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.ScoreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLeaderboardsBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score

class Leaderboards : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardsBinding
    private lateinit var scoreAdapter: ScoreAdapter
    private var scoreArrayList= ArrayList<Score>()
    private lateinit var scoredao: ScoreDAO
    private lateinit var quizdao: QuizDAO
    private lateinit var user : String
    private var curr_user = "tabbeee"
    private var quizID = "voTDkBpCxCDgGitwt012"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardsBinding.inflate(layoutInflater)
        scoredao = ScoreDAO(applicationContext)
        quizdao = QuizDAO(applicationContext)
        setContentView(binding.root)
        populateScores()


        val curr_user = "tabbeee"

        binding.scoreList.layoutManager = LinearLayoutManager(applicationContext)
        scoreAdapter = ScoreAdapter(applicationContext, scoreArrayList, curr_user)
        binding.scoreList.adapter = scoreAdapter
    }

   fun populateScores(){
       lifecycleScope.launch(Dispatchers.IO){
           getQuizName()
           val tempList = async{scoredao.getScores(quizID)}
           println("LOG: TempList has size ${tempList.await()!!.size}")
           if (tempList.await() != null) {
               scoreArrayList = tempList.await()!!
               println(scoreArrayList.size)
               withContext(Dispatchers.Main){
                   binding.scoreList.layoutManager = LinearLayoutManager(applicationContext)
                   scoreAdapter = ScoreAdapter(applicationContext, scoreArrayList, curr_user!!)
                   binding.scoreList.adapter = scoreAdapter
               }
           }
           else{
               println("ERROR: No scores found!")
           }

       }
   }

    suspend fun getQuizName(){
        var quiz = quizdao.getQuizById(quizID)
        var name = quiz!!.quiz_name
        binding.textQuizName.text = name
    }
}