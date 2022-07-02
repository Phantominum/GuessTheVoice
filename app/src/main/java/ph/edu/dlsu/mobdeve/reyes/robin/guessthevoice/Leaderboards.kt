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
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.ScoreView

class Leaderboards : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardsBinding
    private lateinit var scoreAdapter: ScoreAdapter
    private var scoreArrayList= ArrayList<ScoreView>()
    private lateinit var userDao: UserDAO
    private lateinit var scoredao: ScoreDAO
    private lateinit var quizdao: QuizDAO
    private lateinit var email : String


    private var quizID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardsBinding.inflate(layoutInflater)
        scoredao = ScoreDAO(applicationContext)
        quizdao = QuizDAO(applicationContext)
        userDao = UserDAO(applicationContext)
        setContentView(binding.root)

        println("IN LEADERBOARDS ACTIVITY")
        if (intent.extras != null){
            val bundle = intent.extras
            email = bundle!!.getString("email").toString()
            quizID = bundle!!.getString("quizID").toString()
            println("LOG: Received quiz ID: ${quizID}")
            println("LOG: Received email ${email}")
        }

        populateScores()

        binding.scoreList.layoutManager = LinearLayoutManager(applicationContext)
        scoreAdapter = ScoreAdapter(applicationContext, scoreArrayList, email)
        binding.scoreList.adapter = scoreAdapter
    }

   fun populateScores(){
       lifecycleScope.launch(Dispatchers.IO){
           val quiz_name = getQuizName()
           val scoreJob = async{ scoredao.getScores(quizID) }
           val scores = scoreJob.await()
           println("LOG: TempList has size ${scoreJob.await()!!.size}")
           if (scoreJob.await() != null) {
               for (score in scores!!) {
                   val userJob = async{ userDao.getAccount(score.username) }
                   val user = userJob.await()
                   val scoreView = ScoreView(score, user!!.username)
                   scoreArrayList.add(scoreView)
               }

               println(scoreArrayList.size)
               withContext(Dispatchers.Main){
                   binding.textQuizName.text = quiz_name
                   binding.scoreList.layoutManager = LinearLayoutManager(applicationContext)
                   scoreAdapter = ScoreAdapter(applicationContext, scoreArrayList, email!!)
                   binding.scoreList.adapter = scoreAdapter
               }
           }
           else{
               println("ERROR: No scores found!")
           }

       }
   }

    suspend fun getQuizName(): String {
        var quiz = quizdao.getQuizById(quizID)
        return quiz!!.quiz_name
    }
}