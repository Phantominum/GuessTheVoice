package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityViewQuizBinding

class ViewQuizActivity : AppCompatActivity() {
    private lateinit var binding:ActivityViewQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        binding.textViewQuizName.text = "${bundle!!.getString("quiz_name")}"
        binding.textViewQuizCreator.text = "${bundle!!.getString("quiz_creator")}"
        binding.viewQuizImage.setImageResource(bundle.getInt("quiz_image"))

        binding.buttonPlayQuiz.setOnClickListener{

            var goToTakeQuiz = Intent(this, TakeQuizActivity::class.java)
            startActivity(goToTakeQuiz)
        }

        binding.buttonViewLeaderboards.setOnClickListener{
            var goToLeaderboards = Intent(this, Leaderboards::class.java)
            startActivity(goToLeaderboards)
        }

    }
}