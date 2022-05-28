package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.ScoreAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.ScoreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.ScoreDAOArrayImpl
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLeaderboardsBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score

class Leaderboards : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardsBinding
    private lateinit var scoreAdapter: ScoreAdapter
    private lateinit var scoreArrayList: ArrayList<Score>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val curr_user = "tabbeee"

        binding.scoreList.layoutManager = LinearLayoutManager(applicationContext)
        scoreAdapter = ScoreAdapter(applicationContext, scoreArrayList, curr_user)
        binding.scoreList.adapter = scoreAdapter

        print("Size is ${scoreArrayList.size}")
        print(scoreArrayList)
    }

    private fun init() {
        val dao: ScoreDAO = ScoreDAOArrayImpl()
        var score = Score()
        score.rank = 1
        score.username = "vissariown"
        score.points = 25
        dao.addScore(score)

        score = Score()
        score.rank = 2
        score.username = "tabbeee"
        score.points = 20
        dao.addScore(score)
        scoreArrayList = dao.getScores()
    }
}