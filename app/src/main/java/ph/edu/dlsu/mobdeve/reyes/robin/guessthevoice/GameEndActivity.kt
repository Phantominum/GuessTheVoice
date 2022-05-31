package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityGameEndBinding

class GameEndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGameEndBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonQuizEndBack.setOnClickListener{
            var gotoDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(gotoDashboard)
        }

        binding.buttonQuizEndViewLeaderboards.setOnClickListener{
            var gotoLeaderboards = Intent(this, Leaderboards::class.java)
            startActivity(gotoLeaderboards)
        }
    }
}