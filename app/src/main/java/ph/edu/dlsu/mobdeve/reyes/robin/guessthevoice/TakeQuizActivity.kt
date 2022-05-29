package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityTakeQuizBinding

class TakeQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakeQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTakeQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.skipButton.setOnClickListener{

            var goToGameEnd = Intent(this, GameEndActivity::class.java)
            startActivity(goToGameEnd)
        }

    }
}